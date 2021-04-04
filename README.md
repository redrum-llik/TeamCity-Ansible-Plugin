# Ansible plugin for TeamCity

This project aims to provide a simple Ansible playbook runner for the TeamCity. The key features are:

* custom [callback plugin](https://docs.ansible.com/ansible/latest/plugins/callback.html) which allows to inject TeamCity service messages and format the build log
* automatic detection of `ansible-playbook` executable on the agent side
* ability to pass system parameters as `--extra-vars`

The features which are planned:
* some sort of simple HTML report based on the playbook output/results to be provided as a report tab

Example of build log output:
![image](https://user-images.githubusercontent.com/63649969/113508315-fb8dce00-9557-11eb-84ac-27e93dbb3ced.png)

## Requirements

Installed Ansible on the agent side with Python 3.X. 

# Configuration

## Ansible Parameters

**Playbook source**: defines the source of playbook for this runner. The playbook may be loaded from file, or may be defined inline.

**Inventory**: defines the inventory file path.

**Additional arguments**: any extra arguments to be passed to the command.

## Run Parameters

**Fail if changes detected**: add `--check` to the command arguments, and instruct callback plugin (see below) to raise a build problem if any changes are detected.

**Force colored log**: inject `ANSIBLE_FORCE_COLOR` into the execution context.

## Docker Settings

See the relevant information on the [Docker Wrapper](https://www.jetbrains.com/help/teamcity/docker-wrapper.html) documentation page.

# Implementation details

## Callback plugin

The runner supplies a simple [callback plugin](https://docs.ansible.com/ansible/latest/plugins/callback.html) which affects output of `ansible-playbook`, and will inject the following environment variables into the execution context:

* `ANSIBLE_STDOUT_CALLBACK` = `teamcity_callback` - this will override the stdout callback plugin with the one provided with this runner;
* `ANSIBLE_CALLBACK_PLUGINS` - the path will be updated to also include the folder on the agent side containing above plugin.

## Ansible detection

The detection logic will look up the `ansible-playbook` executable in PATH as well as in any folder defined in `teamcity.ansible.detector.search.path` agent property. All found instances are stored in the configuration variables of the agent (see `ansible.*` variables).

Runner will impose the following [agent requirements](https://www.jetbrains.com/help/teamcity/agent-requirements.html):

* `ansible.pythonversion` >= 3.0.0
* `ansible.path` exists

## Prefixed parameters

Any system build parameter which starts with `system.ansible.` prefix will be exported into a temporary JSON file which will be supplied as `--extra-vars` value which should allow to easily pass TeamCity parameters into the playbook context, even if it is defined in the file. 
