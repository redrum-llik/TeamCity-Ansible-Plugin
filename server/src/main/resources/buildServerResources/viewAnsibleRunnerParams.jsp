<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="forms" tagdir="/WEB-INF/tags/forms" %>
<%@ taglib prefix="l" tagdir="/WEB-INF/tags/layout" %>
<%@ taglib prefix="bs" tagdir="/WEB-INF/tags" %>
<jsp:useBean id="bean" class="jetbrains.buildServer.runner.ansibleRunner.AnsibleBean"/>
<jsp:useBean id="propertiesBean" scope="request" type="jetbrains.buildServer.controllers.BasePropertiesBean"/>

<c:if test="${propertiesBean.properties[bean.playbookModeKey] eq bean.playbookModeFile}">
    <div class="parameter">
        Playbook: <props:displayValue name="${bean.playbookFileKey}" />
    </div>
</c:if>

<c:if test="${propertiesBean.properties[bean.playbookModeKey] eq bean.playbookModeYaml}">
    <div class="parameter">
        Playbook YAML: <props:displayValue name="${bean.playbookYamlKey}" />
    </div>
</c:if>

<div class="parameter">
    Inventory: <props:displayValue name="${bean.inventoryFileKey}" />
</div>

<div class="parameter">
    Additional arguments: <props:displayValue name="${bean.extraArgsKey}" />
</div>

<div class="parameter">
    Do a dry run: <props:displayCheckboxValue name="${bean.dryRunKey}" checkedValue="Yes" uncheckedValue="No"/>
</div>

<div class="parameter">
    Fail build on changes: <props:displayCheckboxValue name="${bean.failOnChangesKey}" checkedValue="Yes" uncheckedValue="No"/>
</div>

<div class="parameter">
    Use colored build: <props:displayCheckboxValue name="${bean.coloredBuildLogKey}" checkedValue="Yes" uncheckedValue="No"/>
</div>