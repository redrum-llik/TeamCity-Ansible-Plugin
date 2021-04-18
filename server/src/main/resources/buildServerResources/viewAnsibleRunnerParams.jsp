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

<props:viewWorkingDirectory />

<c:if test="${propertiesBean.properties[playbookBean.key] == playbookBean.fileModeKey}">
    <div class="parameter">
        ${playbookBean.fileLabel} <props:displayValue name="${playbookBean.fileKey}" />
    </div>
</c:if>

<c:if test="${propertiesBean.properties[playbookBean.key] == playbookBean.yamlModeKey}">
    <div class="parameter">
        ${playbookBean.yamlLabel} <props:displayValue name="${playbookBean.yamlKey}" />
    </div>
</c:if>

<div class="parameter">
    ${inventoryBean.label} <props:displayValue name="${inventoryBean.key}" />
</div>

<c:set var="additionalArguments" value="${propertiesBean.properties[additionalArgumentsBean.key]}"/>

<c:if test="${not empty additionalArguments}">
    <div class="parameter">
            ${additionalArgumentsBean.label} <props:displayValue name="${additionalArgumentsBean.key}" />
    </div>
</c:if>

<div class="parameter">
    ${failIfChangesBean.label} <props:displayCheckboxValue name="${failIfChangesBean.key}" checkedValue="Yes" uncheckedValue="No"/>
</div>

<div class="parameter">
    ${forceColoredLogBean.label} <props:displayCheckboxValue name="${forceColoredLogBean.key}" checkedValue="Yes" uncheckedValue="No"/>
</div>

<div class="parameter">
    ${passSystemParametersBean.label} <props:displayCheckboxValue name="${passSystemParametersBean.key}" checkedValue="Yes" uncheckedValue="No"/>
</div>