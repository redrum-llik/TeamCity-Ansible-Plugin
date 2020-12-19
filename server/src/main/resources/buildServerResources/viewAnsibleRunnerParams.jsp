<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%--<jsp:useBean id="propertiesBean" scope="request" type="jetbrains.buildServer.controllers.BasePropertiesBean"/>--%>

<%@include file="globalConsts.jsp" %>
<%@include file="rakeRunnerConsts.jsp" %>

<div class="parameter">
    Rakefile:
    <c:choose>
        <c:when test="${empty propertiesBean.properties[USE_CUSTOM_BUILD_FILE_KEY]}">
            <props:displayValue name="${BUILD_FILE_PATH_KEY}" emptyValue="not specified"/>
        </c:when>
        <c:otherwise>
            <props:displayValue name="${BUILD_FILE_KEY}" emptyValue="<empty>" showInPopup="true" popupTitle="Rakefile content"
                                popupLinkText="view Rakefile content" syntax="rake"/>
        </c:otherwise>
    </c:choose>
</div>

<props:viewWorkingDirectory/>

<div class="parameter">
    Rake tasks: <strong><props:displayValue name="${UI_RAKE_TASKS_PROPERTY}" emptyValue="default"/></strong>
</div>

<div class="parameter">
    Additional Rake command line parameters: <strong><props:displayValue
        name="${UI_RAKE_ADDITIONAL_CMD_PARAMS_PROPERTY}" emptyValue="not specified"/></strong>
</div>

<div class="parameter">
    Launching Parameters:
    <div class="nestedParameter">Env preparation type: <strong><props:displayValue
            name="${UI_RUBY_USAGE_MODE}" emptyValue="default / REC feature"/></strong></div>
    <div class="nestedParameter">Ruby interpreter path: <strong><props:displayValue
            name="${UI_RUBY_INTERPRETER_PATH}" emptyValue="will be searched in the PATH environment variable"/></strong></div>
    <div class="nestedParameter">RVM sdk name: <strong><props:displayValue
            name="${UI_RUBY_RVM_SDK_NAME}" emptyValue="not specified"/></strong></div>
    <div class="nestedParameter">RVM gemset name: <strong><props:displayValue
            name="${UI_RUBY_RVM_GEMSET_NAME}" emptyValue="not specified"/></strong></div>
    <div class="nestedParameter">Bundler: bundle exec: <strong><props:displayCheckboxValue
            name="${UI_BUNDLE_EXEC_PROPERTY}"/></strong></div>
    <div class="nestedParameter">Track invoke/execute stages: <strong><props:displayCheckboxValue
            name="${UI_RAKE_TRACE_INVOKE_EXEC_STAGES_ENABLED}"/></strong></div>
</div>

<div class="parameter">
    Attached reporters:
    <div class="nestedParameter">Test::Unit: <strong><props:displayCheckboxValue name="${UI_RAKE_TESTUNIT_ENABLED_PROPERTY}"/></strong></div>
    <div class="nestedParameter">Test-Spec: <strong><props:displayCheckboxValue name="${UI_RAKE_TESTSPEC_ENABLED_PROPERTY}"/></strong></div>
    <div class="nestedParameter">Shoulda: <strong><props:displayCheckboxValue name="${UI_RAKE_SHOULDA_ENABLED_PROPERTY}"/></strong></div>

    <div class="nestedParameter">RSpec: <strong><props:displayCheckboxValue name="${UI_RAKE_RSPEC_ENABLED_PROPERTY}"/></strong>

        <div class="nestedParameter">RSpec options(SPEC_OPTS): <strong><props:displayValue
                name="${UI_RAKE_RSPEC_OPTS_PROPERTY}" emptyValue="not specified"/></strong></div>
    </div>

    <div class="nestedParameter">Cucumber: <strong><props:displayCheckboxValue name="${UI_RAKE_CUCUMBER_ENABLED_PROPERTY}"/></strong>

        <div class="nestedParameter">Cucumber options(CUCUMBER_OPTS): <strong><props:displayValue
                name="${UI_RAKE_CUCUMBER_OPTS_PROPERTY}" emptyValue="not specified"/></strong></div>
    </div>
</div>