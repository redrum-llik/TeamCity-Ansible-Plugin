# Ansible plugin for TeamCity

This project aims to provide a simple Ansible playbook runner for the TeamCity. The key features are:

* custom callback plugin which allows to inject TeamCity service messages and format the build log
* automatic detection of Ansible executable on the agent side
* ability to pass configuration and system parameters as `--extra-vars`

The features which are planned:
* some sort of simple HTML report based on the playbook output/results to be provided as a report tab

## Requirements

Installed Ansible on the agent side with Python 3.X. 

## Implementation details

Runner will make the following changes to the execution context:

* `ANSIBLE_STDOUT_CALLBACK` = `teamcity_callback` - this will override the stdout callback plugin with the one provided with this runner;
* `ANSIBLE_CALLBACK_PLUGINS` - the path will be updated to also include the folder on the agent side containing above plugin.

The detection logic will look up the ansible-playbook executable in PATH as well as in any folder defined in `teamcity.ansible.detector.search.path` agent property. All found instances are stored in the configuration variables of the agent (see `ansible.*` variables).

If a dry run option is chosen in the runner configuration and any changes are detected, step will fail. 
