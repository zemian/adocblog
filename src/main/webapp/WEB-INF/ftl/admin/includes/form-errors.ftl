<#if errors??>
    <ul class="alert alert-danger">
    <#list errors as error>
        <li>${error}</li>
    </#list>
    </ul>
</#if>
