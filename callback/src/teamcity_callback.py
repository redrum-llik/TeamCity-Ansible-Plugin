from jinja2 import Template, Environment, FileSystemLoader

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

FAIL_ON_CHANGES_ENVVAR = 'ANSIBLE_TEAMCITY_FAIL_ON_CHANGES'
REPORT_PATH_ENVVAR = 'ANSIBLE_TEAMCITY_REPORT_PATH'
REPORT_TEMPLATE_NAME = 'ansibleReport.html.tmpl'


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
        self._fail_on_changes = os.getenv(FAIL_ON_CHANGES_ENVVAR)
        self._report_path = os.getenv(REPORT_PATH_ENVVAR)
        if self._report_path:
            self._service_message_debug_log(f'Writing Ansible changes report to the {self._report_path}')
            self._report = AnsibleReport(self._report_path)

    @property
    def report_enabled(self):
        if self._report_path:
            return True
        return False

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

    def _service_message_debug_log(self, msg: str):
        escaped_message = escape_value(msg)
        sys.stdout.write(f"##teamcity[message text='{escaped_message}' status='NORMAL']")

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
        if result.is_changed():
            msg = f"Changes in <{result.task_name}> task for <{result._host}> host"
            if self._report:
                self._report.add_change(result.task_name, result._host)
            if self._fail_on_changes:
                self._service_message_build_problem(msg, result.task_name)

    def v2_playbook_on_stats(self, stats: AggregateStats):
        self._emit_task_block_closed()
        if self._report:
            self._report.render_to_file()
        super().v2_playbook_on_stats(stats)


class AnsibleReport():
    def __init__(self, report_path: str):
        self._changes = {}
        self._report_path = report_path

        callback_folder = os.path.dirname(
            os.path.realpath(__file__)
        )
        fs_loader = FileSystemLoader(searchpath=callback_folder)
        env = Environment(loader=fs_loader, autoescape=True)
        self._template = env.get_template(REPORT_TEMPLATE_NAME)

    @property
    def changes(self) -> dict:
        return self._changes

    def task_changes(self, task_name: str):
        task_name_string = str(task_name)
        if task_name_string not in self.changes.keys():
            self.changes[task_name_string] = set()
        return self.changes[task_name_string]

    def add_change(self, task_name, host_name):
        host_name_string = str(host_name)
        self.task_changes(task_name).add(host_name_string)

    def render_to_file(self):
        output = self._template.render(changes=self._changes)
        with open(self._report_path, 'w') as fh:
            fh.write(output)
            fh.flush()
