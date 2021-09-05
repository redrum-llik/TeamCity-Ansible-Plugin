# Ansible plugin for TeamCity

This project aims to provide a simple Ansible-related build feature for TeamCity. The key features are:

* custom [callback plugin](https://docs.ansible.com/ansible/latest/plugins/callback.html) which allows to: 
  * inject TeamCity service messages and format the build log
  * provide custom report tab to show the changed tasks and hosts where the changes happened
* ability to pass system parameters as `--extra-vars`

Example of the report tab (to be prettified):

![image](https://user-images.githubusercontent.com/63649969/132141561-7324b4fc-12e8-4b78-b544-92f0c808f62e.png)

The usage scenario is to make a dry run as a first build, allow to check it manually easier and, if the changes look good, apply them in the second build. There is a planned feature to allow to request approval from a sufficiently permissioned user before starting the other build. 

## Requirements

Callback plugin requires Ansible with Python 3.X. 

# Configuration

**Create a build problem for any detected change**: for any detected change, create a build problem to fail the build. 

**Force colored log**: enforce the colored output for Ansible commands.

**Save system properties to file**: save system build parameters into a file, formatted for usage with `--extra-vars` argument.

# Implementation details

## Callback plugin

The runner supplies a simple [callback plugin](https://docs.ansible.com/ansible/latest/plugins/callback.html) which affects the output of `ansible-playbook`, and will inject the following environment variables into the execution context:

* `ANSIBLE_STDOUT_CALLBACK` = `teamcity_callback` — this will override the stdout callback plugin with the one provided by this runner.
* `ANSIBLE_CALLBACK_PLUGINS` — the path will be updated to also include the directory with the above plugin on the agent side.

## System properties

If a corresponding option is enabled, system properties will be exported into a temporary JSON file. This file will be supplied as the `--extra-vars` value. The dots (`.`) in property name are replaced with underscores (`_`) to provide a valid variable identifier. 
If the additional arguments field contains the `--extra-vars` argument too, the logic will be skipped.
