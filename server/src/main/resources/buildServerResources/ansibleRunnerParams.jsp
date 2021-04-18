<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="forms" tagdir="/WEB-INF/tags/forms" %>
<%@ taglib prefix="l" tagdir="/WEB-INF/tags/layout" %>
<%@ taglib prefix="bs" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="additionalArgumentsBean" class="jetbrains.buildServer.runner.ansibleRunner.beans.AdditionalArgumentsBean"/>
<jsp:useBean id="failIfChangesBean" class="jetbrains.buildServer.runner.ansibleRunner.beans.FailIfChangesBean"/>
<jsp:useBean id="forceColoredLogBean" class="jetbrains.buildServer.runner.ansibleRunner.beans.ForceColoredLogBean"/>
<jsp:useBean id="inventoryBean" class="jetbrains.buildServer.runner.ansibleRunner.beans.InventoryBean"/>
<jsp:useBean id="passSystemParametersBean" class="jetbrains.buildServer.runner.ansibleRunner.beans.PassSystemParametersBean"/>
<jsp:useBean id="playbookBean" class="jetbrains.buildServer.runner.ansibleRunner.beans.PlaybookBean"/>
<jsp:useBean id="propertiesBean" scope="request" type="jetbrains.buildServer.controllers.BasePropertiesBean"/>

<forms:workingDirectory/>

<l:settingsGroup title="Ansible Parameters">
    <tr>
        <th>${playbookBean.label}</th>
        <td>
            <props:selectProperty name="${playbookBean.key}" id="playbook_option" className="shortField"
                                  onchange="BS.Ansible.updatePlaybookType()">
                <props:option value="${playbookBean.fileModeKey}">${playbookBean.fileModeValue}</props:option>
                <props:option value="${playbookBean.yamlModeKey}">${playbookBean.yamlModeValue}</props:option>
            </props:selectProperty>
            <span class="error" id="error_${playbookBean.key}"></span>
        </td>
    </tr>

    <tr id="playbook_yaml">
        <th><label for="${playbookBean.yamlKey}">${playbookBean.yamlLabel}<l:star/></label></th>
        <td class="codeHighlightTD">
            <props:multilineProperty name="${playbookBean.yamlKey}" linkTitle="${playbookBean.yamlDescription}" cols="58"
                                     rows="10" highlight="yaml" expanded="${true}"/>
        </td>
    </tr>

    <tr id="playbook_file">
        <th><label for="${playbookBean.fileKey}">${playbookBean.fileLabel}<l:star/></label></th>
        <td>
            <props:textProperty name="${playbookBean.fileKey}" className="longField"/>
            <bs:vcsTree fieldId="${playbookBean.fileKey}"/>
            <span class="smallNote">${playbookBean.fileDescription}</span>
        </td>
    </tr>

    <tr id="inventory_file">
        <th><label for="${inventoryBean.key}">${inventoryBean.label}</label></th>
        <td>
            <props:textProperty name="${inventoryBean.key}" className="longField"/>
            <bs:vcsTree fieldId="${inventoryBean.key}"/>
            <span class="smallNote">${inventoryBean.description}</span>
        </td>
    </tr>

    <tr id="options">
        <th><label for="${additionalArgumentsBean.key}">${additionalArgumentsBean.label}</label></th>
        <td>
            <props:textProperty name="${additionalArgumentsBean.key}" className="longField"/>
            <span class="smallNote">${additionalArgumentsBean.description}</span>
        </td>
    </tr>
</l:settingsGroup>

<l:settingsGroup title="Run Parameters" className="advancedSetting">
    <tr class="advancedSetting">
        <th><label>${failIfChangesBean.label}</label></th>
        <td><props:checkboxProperty name="${failIfChangesBean.key}"/>
            <label for="${failIfChangesBean.key}">${failIfChangesBean.description}</label>
            <br/>
        </td>
    </tr>

    <tr class="advancedSetting">
        <th><label>${forceColoredLogBean.label}</label></th>
        <td><props:checkboxProperty name="${forceColoredLogBean.key}"/>
            <label for="${forceColoredLogBean.key}">${forceColoredLogBean.description}</label>
            <br/>
        </td>
    </tr>

    <tr class="advancedSetting">
        <th><label>${passSystemParametersBean.label}</label></th>
        <td><props:checkboxProperty name="${passSystemParametersBean.key}"/>
            <label for="${passSystemParametersBean.key}">${passSystemParametersBean.description}</label>
            <br/>
        </td>
    </tr>
</l:settingsGroup>

<script type="text/javascript">
    BS.Ansible = {
        updatePlaybookType : function () {
            const val = $('playbook_option').value;
            if (val === '${playbookBean.yamlModeKey}') {
                BS.Util.hide($('playbook_file'));
                BS.Util.show($('playbook_yaml'));
            }
            if (val === '${playbookBean.fileModeKey}') {
                BS.Util.hide($('playbook_yaml'))
                BS.Util.show($('playbook_file'))
            }
            BS.MultilineProperties.updateVisible()
        }
    }

    BS.Ansible.updatePlaybookType();
</script>