<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<jsp:useBean id="buildProblemBean" class="jetbrains.buildServer.ansibleSupportPlugin.beans.BuildProblemOnChangesBean"/>
<jsp:useBean id="systemPropertiesBean" class="jetbrains.buildServer.ansibleSupportPlugin.beans.SystemPropertiesBean"/>
<jsp:useBean id="forceColoredLogBean" class="jetbrains.buildServer.ansibleSupportPlugin.beans.ForceColoredLogBean"/>

<tr>
    <td colspan="2">
        <em>Provides an Ansible callback plugin to process the ansible-playbook output.</em>
    </td>
</tr>
<tr class="noBorder">
    <th><label for="${buildProblemBean.key}">${buildProblemBean.label}</label></th>
    <td>
        <props:checkboxProperty name="${buildProblemBean.key}"/>
    </td>
</tr>
<tr class="noBorder">
    <th><label for="${forceColoredLogBean.key}">${forceColoredLogBean.label}</label></th>
    <td>
        <props:checkboxProperty name="${forceColoredLogBean.key}"/>
        <span class="smallNote">${forceColoredLogBean.description}</span>
    </td>
</tr>
<tr class="noBorder">
    <th><label for="${systemPropertiesBean.key}">${systemPropertiesBean.label}</label></th>
    <td>
        <props:textProperty name="${systemPropertiesBean.key}" className="longField"/>
        <span class="smallNote">${systemPropertiesBean.description}</span>
    </td>
</tr>