import sys

from ansible.plugins.callback import CallbackBase
from ansible.plugins.callback.default import CallbackModule as Default


class CallbackModule(Default):
    CALLBACK_VERSION = 1.0
    CALLBACK_TYPE = 'stdout'
    CALLBACK_NAME = 'teamcity_callback'

    def __init__(self):
        super(CallbackModule, self).__init__()
        sys.stdout.write(self.get_option('show_per_host_start'))

    def _emit_task_block_opened(self):
        sys.stdout.write(f"##teamcity[blockOpened name='{self._last_task_name}']")
        sys.stdout.flush()

    def _emit_task_block_closed(self):
        if self._last_task_name:
            sys.stdout.write(f"##teamcity[blockClosed name='{self._last_task_name}']")
            sys.stdout.flush()

    def _print_task_banner(self, task):
        self._emit_task_block_closed()
        self._emit_task_block_opened()
        super()._print_task_banner(task)

    # def v2_playbook_on_task_start(self, task, is_conditional):
    #     self._emit_task_block_closed()
    #     self._emit_task_block_opened(task)

    def v2_playbook_on_stats(self, stats):
        self._emit_task_block_closed()
        super().v2_playbook_on_stats(stats)

