<div class="form-group">
    <label for="category" class="col-sm-2 control-label">Category</label>
    <div class="col-sm-10">
        <input type="text" class="form-control" id="category" name="category" value="${setting.category!''}">
    </div>
</div>
<div class="form-group">
    <label for="name" class="col-sm-2 control-label">Name</label>
    <div class="col-sm-10">
        <input type="text" class="form-control" id="name" name="name" value="${setting.name!''}">
    </div>
</div>
<div class="form-group">
    <label for="value" class="col-sm-2 control-label">Value</label>
    <div class="col-sm-10">
        <input type="text" class="form-control" id="value" name="value" value="${setting.value!''}">
    </div>
</div>
<div class="form-group">
    <label for="type" class="col-sm-2 control-label">Type</label>
    <div class="col-sm-10">
        <select class="form-control" id="type" name="type">
            <option <#if (setting.type!'') == 'STRING'>selected </#if>value="STRING">STRING</option>
            <option <#if (setting.type!'') == 'BOOLEAN'>selected </#if>value="BOOLEAN">BOOLEAN</option>
            <option <#if (setting.type!'') == 'INTEGER'>selected </#if>value="INTEGER">INTEGER</option>
            <option <#if (setting.type!'') == 'DOUBLE'>selected </#if>value="DOUBLE">DOUBLE</option>
            <option <#if (setting.type!'') == 'LIST'>selected </#if>value="LIST">LIST</option>
        </select>
    </div>
</div>
<div class="form-group">
    <label for="description" class="col-sm-2 control-label">Description</label>
    <div class="col-sm-10">
        <input type="text" class="form-control" id="description" name="description" value="${setting.description!''}">
    </div>
</div>
