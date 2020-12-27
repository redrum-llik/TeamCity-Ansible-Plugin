import os
import sys

from ansible.executor.stats import AggregateStats
from ansible.executor.task_result import TaskResult
from ansible.playbook.task import Task
from ansible.plugins.callback.default import CallbackModule as Default


class CallbackModule(Default):
    CALLBACK_VERSION = 1.0
    CALLBACK_TYPE = 'stdout'
    CALLBACK_NAME = 'teamcity_callback'

    def __init__(self):
        super(CallbackModule, self).__init__()
        self._last_task_block_name = None
        self._is_task_block_open = False
        self._fail_on_changes = os.getenv("ANSIBLE_TEAMCITY_FAIL_ON_CHANGES")

    def get_option(self, k):
        try:
            return super().get_option(k)
        except KeyError:
            return None

    def _emit_task_block_opened(self, task: Task):
        self._last_task_block_name = task.get_name()
        sys.stdout.write(f"##teamcity[blockOpened name='{self._last_task_block_name}']")
        sys.stdout.flush()
        self._is_task_block_open = True

    def _emit_task_block_closed(self):
        if self._last_task_block_name and self._is_task_block_open:
            sys.stdout.write(f"##teamcity[blockClosed name='{self._last_task_block_name}']")
            sys.stdout.flush()
            self._is_task_block_open = False

    def _emit_build_problem(self, msg: str, identity: str = None):
        identity_string = f"identity='{identity}'" if identity else None
        sys.stdout.write(f"##teamcity[buildProblem description='{msg}' {identity_string}]")
        sys.stdout.flush()

    def _print_task_banner(self, task: Task):
        self._emit_task_block_closed()
        self._emit_task_block_opened(task)
        super()._print_task_banner(task)

    def v2_runner_item_on_failed(self, result: TaskResult):
        super().v2_runner_item_on_failed(result)
        msg = f"Task <{result.task_name}> failed on <{result._host}> host"
        self._emit_build_problem(msg, result.task_name)

    def v2_runner_on_ok(self, result: TaskResult):
        super().v2_runner_on_ok(result)
        if result.is_changed() and self._fail_on_changes:
            msg = f"Changes in <{result.task_name}> task for <{result._host}> host"
            self._emit_build_problem(msg, result.task_name)

    def v2_playbook_on_stats(self, stats: AggregateStats):
        self._emit_task_block_closed()
        super().v2_playbook_on_stats(stats)