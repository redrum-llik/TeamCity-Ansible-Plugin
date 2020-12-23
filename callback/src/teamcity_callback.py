from ansible.plugins.callback import CallbackBase
import sys


class CallbackModule(CallbackBase):
    def __init__(self):
        self._last_task_name = None

    def v2_playbook_on_task_start(self, task, is_conditional):
        self._last_task_name = task.name
        sys.stdout.write(f"##teamcity[blockOpened name='{self._last_task_name}']")
        sys.stdout.flush()
        super().v2_playbook_on_task_start(task, is_conditional)

    def v2_playbook_on_stats(self, stats):
        if self._last_task_name:
            sys.stdout.write(f"##teamcity[blockClosed name='{self._last_task_name}']")
            sys.stdout.flush()
        super().v2_playbook_on_stats(stats)