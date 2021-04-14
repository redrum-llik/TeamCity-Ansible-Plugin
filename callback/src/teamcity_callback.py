import os
import sys

from ansible.executor.stats import AggregateStats
from ansible.executor.task_result import TaskResult
from ansible.playbook.task import Task
from ansible.plugins.callback.default import CallbackModule as Default

ESCAPE_DICT = {
    "'": "|'",
    "|": "||",
    "\n": "|n",
    "\r": "|r",
    '[': '|[',
    ']': '|]'
}


def escape_value(value):
    return "".join(ESCAPE_DICT.get(x, x) for x in value)


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

    def _service_message_block_open(self, name: str):
        escaped_name = escape_value(name)
        sys.stdout.write(f"##teamcity[blockOpened name='{escaped_name}']")
        sys.stdout.flush()

    def _service_message_block_close(self, name: str):
        escaped_name = escape_value(name)
        sys.stdout.write(f"##teamcity[blockClosed name='{escaped_name}']")
        sys.stdout.flush()

    def _service_message_build_problem(self, msg: str, identity: str = None):
        escaped_message = escape_value(msg)
        identity_string = f"identity='{escape_value(identity)}'" if identity else None
        sys.stdout.write(f"##teamcity[buildProblem description='{escaped_message}' {identity_string}]")
        sys.stdout.flush()

    def _emit_task_block_opened(self, task: Task):
        self._last_task_block_name = task.get_name()
        self._service_message_block_open(self._last_task_block_name)
        self._is_task_block_open = True

    def _emit_task_block_closed(self):
        if self._last_task_block_name and self._is_task_block_open:
            self._service_message_block_close(self._last_task_block_name)
            self._is_task_block_open = False

    def _print_task_banner(self, task: Task):
        self._emit_task_block_closed()
        self._emit_task_block_opened(task)
        super()._print_task_banner(task)

    def v2_runner_item_on_failed(self, result: TaskResult):
        super().v2_runner_item_on_failed(result)
        msg = f"Task <{result.task_name}> failed on <{result._host}> host"
        self._service_message_build_problem(msg, result.task_name)

    def v2_runner_on_ok(self, result: TaskResult):
        super().v2_runner_on_ok(result)
        if result.is_changed() and self._fail_on_changes:
            msg = f"Changes in <{result.task_name}> task for <{result._host}> host"
            self._service_message_build_problem(msg, result.task_name)

    def v2_playbook_on_stats(self, stats: AggregateStats):
        self._emit_task_block_closed()
        super().v2_playbook_on_stats(stats)
