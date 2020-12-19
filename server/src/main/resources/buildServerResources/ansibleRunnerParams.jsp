<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="bs" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="l" tagdir="/WEB-INF/tags/layout" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="forms" tagdir="/WEB-INF/tags/forms" %>

<%--<jsp:useBean id="propertiesBean" scope="request" type="jetbrains.buildServer.controllers.BasePropertiesBean"/>--%>
<%@include file="globalConsts.jsp" %>
<%@include file="rakeRunnerConsts.jsp" %>

<%--Default initial settings format version--%>
<props:hiddenProperty name="${CONFIGURATION_VERSION_PROPERTY}" value="${CONFIGURATION_VERSION_CURRENT}"/>

<style type="text/css">
    .rvm_options {
        padding-top: 3px;
    }

    .rvm_options_editor {
        padding-top: 2px;
    }

    .rake_reporter {
        padding-top: 3px;
    }

    .rake_reporter_options {
        padding-top: 2px;
    }
</style>

<l:settingsGroup title="Rake Parameters">
    <tr>
        <th>
            <c:set var="onclick">
                if (this.checked) {
                $('${BUILD_FILE_PATH_KEY}').focus();
                }
            </c:set>
            <props:radioButtonProperty name="${USE_CUSTOM_BUILD_FILE_KEY}" value="" id="custom1"
                                       checked="${empty propertiesBean.properties[USE_CUSTOM_BUILD_FILE_KEY]}" onclick="${onclick}"/>
            <label for="custom1">Path to a Rakefile:</label>
        </th>
        <td>
            <props:textProperty name="${BUILD_FILE_PATH_KEY}" className="longField">
                <jsp:attribute name="afterTextField"><bs:vcsTree fieldId="${BUILD_FILE_PATH_KEY}"/></jsp:attribute>
            </props:textProperty>
            <span class="error" id="error_${BUILD_FILE_PATH_KEY}"></span>
            <span class="smallNote">Enter Rakefile path if you don't want to use a default one. Specified path should be relative to the checkout directory.</span>
        </td>
    </tr>
    <tr>
        <th>
            <c:set var="onclick">
                if (this.checked) {
                try {
                BS.MultilineProperties.show('${BUILD_FILE_KEY}', true);
                $('${BUILD_FILE_KEY}').focus();
                } catch(e) {}
                }
            </c:set>
            <props:radioButtonProperty name="${USE_CUSTOM_BUILD_FILE_KEY}" value="true" id="custom2" onclick="${onclick}"/>
            <label for="custom2">Rakefile content:</label>
        </th>
        <td class="codeHighlightTD">
            <props:multilineProperty highlight="rake"
                                     expanded="${propertiesBean.properties[USE_CUSTOM_BUILD_FILE_KEY] == true}"
                                     name="${BUILD_FILE_KEY}" rows="10" cols="58" linkTitle="Enter the Rakefile content"
                                     onkeydown="$('custom2').checked = true;" className="longField"/>
        </td>
    </tr>
    <props:workingDirectory />
    <tr>
        <th><label for="${UI_RAKE_TASKS_PROPERTY}">Rake tasks: </label></th>
        <td>
            <props:textProperty name="${UI_RAKE_TASKS_PROPERTY}" className="longField"/>
            <span class="smallNote">Enter task names separated by space character if you don't want to use the 'default' task.<br/>E.g. 'test:functionals' or 'mytask:test mytask:test2'.</span>
        </td>
    </tr>
    <tr class="advancedSetting">
        <th><label for="${UI_RAKE_ADDITIONAL_CMD_PARAMS_PROPERTY}">Additional Rake command line parameters: </label></th>
        <td>
            <props:textProperty name="${UI_RAKE_ADDITIONAL_CMD_PARAMS_PROPERTY}" className="longField" expandable="true"/>
            <span class="smallNote">If not empty, these parameters will be added to 'rake' command line.</span>
        </td>
    </tr>
</l:settingsGroup>

<l:settingsGroup title="Ruby Interpreter">
    <tr>
        <th>
            Mode:
        </th>
        <td>
            <c:set var="modeSelected" value="${propertiesBean.properties[UI_RUBY_USAGE_MODE]}"/>
            <props:selectProperty name="${UI_RUBY_USAGE_MODE}" onchange="BS.RakeRunner.onModeChanged()" enableFilter="true" className="mediumField">
                <props:option value="${MODE_DEFAULT}" currValue="${modeSelected}">&lt;Default&gt;</props:option>
                <props:option value="${MODE_PATH}" currValue="${modeSelected}">Path to interpreter</props:option>
                <props:option value="${MODE_RVM}" currValue="${modeSelected}">RVM interpreter</props:option>
            </props:selectProperty>
        </td>
    </tr>
    <tr id="rr.default.mode.container" style="display: none">
        <td colspan="2">
      <span class="smallNote">E.g., a Ruby interpreter provided by <strong>Ruby Environment Configurator</strong><bs:help file="Ruby+Environment+Configurator"/> build feature. If build feature isn't configured the interpreter
      will be searched in the <strong>PATH</strong> environment variable.</span>
        </td>
    </tr>
    <tr id="rr.interpreter.mode.container" style="display: none">
        <th>
            <label for="${UI_RUBY_INTERPRETER_PATH}">Path to interpreter:</label>
        </th>
        <td>
            <props:textProperty name="${UI_RUBY_INTERPRETER_PATH}" className="longField"/>
        </td>
    </tr>
    <tr id="rr.rvm.mode.interpreter.container" style="display: none">
        <th>
            <label for="${UI_RUBY_RVM_SDK_NAME}">RVM Interpreter:<l:star/></label>
        </th>
        <td>
            <props:textProperty name="${UI_RUBY_RVM_SDK_NAME}" className="longField"/>
            <span class="error" id="error_${UI_RUBY_RVM_SDK_NAME}"></span>
            <span class="smallNote">E.g.: <strong>ruby-1.8.7-p249</strong>, <strong>jruby-1.4.0</strong> or <strong>system</strong></span>
        </td>
    </tr>
    <tr id="rr.rvm.mode.gemset.container" style="display: none">
        <th>
            <label for="${UI_RUBY_RVM_GEMSET_NAME}">RVM Gemset:</label>
        </th>
        <td>
            <props:textProperty name="${UI_RUBY_RVM_GEMSET_NAME}" className="longField"/>
            <span class="smallNote">If not specified the default gemset will be used.</span>
        </td>
    </tr>
</l:settingsGroup>
<l:settingsGroup title="Launching Parameters">
    <tr>
    <tr>
        <th>
            <label>Bundler: </label>
        </th>
        <td>
            <props:checkboxProperty name="${UI_BUNDLE_EXEC_PROPERTY}"/>
            <label for="${UI_BUNDLE_EXEC_PROPERTY}">bundle exec</label>
            <span class="smallNote">If your project uses <strong>Bundler</strong> gem requirements manager, this option will allow you to launch rake tasks using 'bundle exec' command.</span>
        </td>
    </tr>
    <tr class="advancedSetting">
        <th>
            <label>Debug: </label>
        </th>
        <td>
            <props:checkboxProperty name="${UI_RAKE_TRACE_INVOKE_EXEC_STAGES_ENABLED}"/>
            <label for="${UI_RAKE_TRACE_INVOKE_EXEC_STAGES_ENABLED}">Track invoke/execute stages</label>
            <br/>
        </td>
    </tr>
    <tr class="advancedSetting">
        <th>
            <label for="${UI_RUBY_INTERPRETER_ADDITIONAL_PARAMS}">Additional interpreter parameters:</label>
        </th>
        <td>
            <props:textProperty name="${UI_RUBY_INTERPRETER_ADDITIONAL_PARAMS}" className="longField"/>
            <span class="smallNote">Additional parameters for interpreter, useful for JRuby interpreters. E.g. <strong>-J-Xmx512m</strong></span>
        </td>
    </tr>
</l:settingsGroup>

<l:settingsGroup title="Tests Reporting">
    <tr>
        <th>
            <label>Attached reporters:</label>
        </th>

        <td>
                <%-- Test Unit --%>
            <div class="rake_reporter">
                <props:checkboxProperty name="${UI_RAKE_TESTUNIT_ENABLED_PROPERTY}"/>
                <label for="${UI_RAKE_TESTUNIT_ENABLED_PROPERTY}">Test::Unit</label>
            </div>

                <%-- Test-Spec --%>
            <div class="rake_reporter">
                <props:checkboxProperty name="${UI_RAKE_TESTSPEC_ENABLED_PROPERTY}"/>
                <label for="${UI_RAKE_TESTSPEC_ENABLED_PROPERTY}">Test-Spec</label>
            </div>

                <%-- Shoulda --%>
            <div class="rake_reporter">
                <props:checkboxProperty name="${UI_RAKE_SHOULDA_ENABLED_PROPERTY}"/>
                <label for="${UI_RAKE_SHOULDA_ENABLED_PROPERTY}">Shoulda</label>
            </div>

                <%-- RSpec --%>
            <div class="rake_reporter">
                <props:checkboxProperty name="${UI_RAKE_RSPEC_ENABLED_PROPERTY}"/>
                <label for="${UI_RAKE_RSPEC_ENABLED_PROPERTY}">RSpec</label>

                <div class="rake_reporter_options">
                    <props:textProperty name="${UI_RAKE_RSPEC_OPTS_PROPERTY}" className="longField"/>
                    <span class="smallNote">Rake will be invoked with a "SPEC_OPTS={internal options} <strong>{user options}</strong>".</span>
                </div>
            </div>

                <%-- Cucumber --%>
            <div class="rake_reporter">
                <props:checkboxProperty name="${UI_RAKE_CUCUMBER_ENABLED_PROPERTY}"/>
                <label for="${UI_RAKE_CUCUMBER_ENABLED_PROPERTY}">Cucumber</label>

                <div class="rake_reporter_options">
                    <props:textProperty name="${UI_RAKE_CUCUMBER_OPTS_PROPERTY}" className="longField"/>
                    <span class="smallNote">Rake will be invoked with a "CUCUMBER_OPTS={internal options} <strong>{user options}</strong>".</span>
                </div>
            </div>
        </td>
    </tr>
</l:settingsGroup>

<script type="text/javascript">
    BS.RakeRunner = {
        onModeChanged:function () {
            var sel = $('${UI_RUBY_USAGE_MODE}');
            var selectedValue = sel[sel.selectedIndex].value;
            if ('${MODE_DEFAULT}' == selectedValue) {
                BS.Util.show('rr.default.mode.container');
                BS.Util.hide('rr.interpreter.mode.container');
                BS.Util.hide('rr.rvm.mode.interpreter.container');
                BS.Util.hide('rr.rvm.mode.gemset.container');

            } else if ('${MODE_PATH}' == selectedValue) {
                BS.Util.hide('rr.default.mode.container');
                BS.Util.show('rr.interpreter.mode.container');
                BS.Util.hide('rr.rvm.mode.interpreter.container');
                BS.Util.hide('rr.rvm.mode.gemset.container');

                $('${UI_RUBY_INTERPRETER_PATH}').focus();
            } else if ('${MODE_RVM}' == selectedValue) {
                BS.Util.hide('rr.default.mode.container');
                BS.Util.hide('rr.interpreter.mode.container');
                BS.Util.show('rr.rvm.mode.interpreter.container');
                BS.Util.show('rr.rvm.mode.gemset.container');

                $('${UI_RUBY_RVM_SDK_NAME}').focus();
            } else {
                // OMG!!!
            }
            BS.VisibilityHandlers.updateVisibility('mainContent');
        }
    };
    BS.RakeRunner.onModeChanged();
</script>