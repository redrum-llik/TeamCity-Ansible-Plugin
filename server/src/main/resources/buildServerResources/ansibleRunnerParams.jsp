<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="forms" tagdir="/WEB-INF/tags/forms" %>
<%@ taglib prefix="l" tagdir="/WEB-INF/tags/layout" %>
<%@ taglib prefix="bs" tagdir="/WEB-INF/tags" %>
<jsp:useBean id="bean" class="jetbrains.buildServer.runner.ansibleRunner.AnsibleBean"/>
<jsp:useBean id="propertiesBean" scope="request" type="jetbrains.buildServer.controllers.BasePropertiesBean"/>

<forms:workingDirectory/>

<l:settingsGroup title="Ansible Parameters">
    <tr>
        <th>Playbook type:</th>
        <td>
            <props:selectProperty name="${bean.playbookModeKey}" id="playbook_option" className="shortField"
                                  onchange="BS.Ansible.updatePlaybookType()">
                <props:option value="${bean.playbookModeFile}">File</props:option>
                <props:option value="${bean.playbookModeYaml}">YAML</props:option>
            </props:selectProperty>
            <span class="error" id="error_${bean.playbookModeKey}"></span>
        </td>
    </tr>

    <tr id="playbook_yaml">
        <th><label for="${bean.playbookYamlKey}">Playbook YAML:<l:star/></label></th>
        <td class="codeHighlightTD">
            <props:multilineProperty name="${bean.playbookYamlKey}" linkTitle="Enter Playbook YAML content" cols="58"
                                     rows="10" highlight="yaml" expanded="${true}"/>
        </td>
    </tr>

    <tr id="playbook_file">
        <th><label for="${bean.playbookFileKey}">Playbook:<l:star/></label></th>
        <td>
            <props:textProperty name="${bean.playbookFileKey}" className="longField"/>
            <bs:vcsTree fieldId="${bean.playbookFileKey}"/>
            <span class="smallNote">Path to the playbook file, absolute or relative to the working directory</span>
        </td>
    </tr>

    <tr id="inventory_file">
        <th><label for="${bean.inventoryFileKey}">Inventory:</label></th>
        <td>
            <props:textProperty name="${bean.inventoryFileKey}" className="longField"/>
            <bs:vcsTree fieldId="${bean.inventoryFileKey}"/>
            <span class="smallNote">Path to the inventory file, absolute or relative to the working directory</span>
        </td>
    </tr>

    <tr id="options">
        <th><label for="${bean.extraArgsKey}">Additional arguments:</label></th>
        <td>
            <props:textProperty name="${bean.extraArgsKey}" className="longField"/>
            <span class="smallNote">Additional arguments to be passed to the command</span>
        </td>
    </tr>

    <tr class="advancedSetting">
        <th><label>Dry run:</label></th>
        <td><props:checkboxProperty name="${bean.dryRunKey}"/>
            <label for="${bean.dryRunKey}">Do a dry run</label>
            <br/>
        </td>
    </tr>
</l:settingsGroup>

<l:settingsGroup title="Run Parameters" className="advancedSetting">
    <tr class="advancedSetting">
        <th><label>Changes detection:</label></th>
        <td><props:checkboxProperty name="${bean.failOnChangesKey}"/>
            <label for="${bean.failOnChangesKey}">Fail build if changes were detected</label>
            <br/>
        </td>
    </tr>

    <tr class="advancedSetting">
        <th><label>Colored log:</label></th>
        <td><props:checkboxProperty name="${bean.coloredBuildLogKey}"/>
            <label for="${bean.coloredBuildLogKey}">Allow Ansible ANSI-colored execution output</label>
            <br/>
        </td>
    </tr>
</l:settingsGroup>

<script type="text/javascript">
    BS.Ansible = {
        updatePlaybookType : function () {
            const val = $('playbook_option').value;
            if (val === '${bean.playbookModeYaml}') {
                BS.Util.hide($('playbook_file'));
                BS.Util.show($('playbook_yaml'));
            }
            if (val === '${bean.playbookModeFile}') {
                BS.Util.hide($('playbook_yaml'))
                BS.Util.show($('playbook_file'))
            }
            BS.MultilineProperties.updateVisible()
        }
    }

    BS.Ansible.updatePlaybookType();
</script>