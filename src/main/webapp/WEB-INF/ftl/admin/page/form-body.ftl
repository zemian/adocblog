<input type="hidden" name="type" value="${doc.type}">
<div class="form-group">
    <label for="latestContent.title" class="col-sm-2 control-label">Title</label>
    <div class="col-sm-10">
        <input type="text" class="form-control" id="latestContent.title" name="latestContent.title" value="${doc.latestContent.title!''}">
    </div>
</div>

<#if doc.type == 'PAGE'>
<div class="form-group">
    <label for="path" class="col-sm-2 control-label">Path</label>
    <div class="col-sm-10">
        <input type="text" class="form-control" id="path" name="path" value="${doc.path!''}">
    </div>
</div>
</#if>

<div class="form-group">
    <label for="tags" class="col-sm-2 control-label">Tags</label>
    <div class="col-sm-10">
        <input type="text" class="form-control" id="tags" name="tags" value="${doc.tags!''}">
    </div>
</div>
<div class="form-group">
    <label for="latestContent.format" class="col-sm-2 control-label">Format</label>
    <div class="col-sm-10">
        <select class="form-control" id="latestContent.format" name="latestContent.format">
            <#if doc.type == 'BLOG'>
                <option <#if (doc.latestContent.format!'') == 'ADOC'>selected="true"</#if> value="ADOC">ADOC</option>
                <option <#if (doc.latestContent.format!'') == 'HTML'>selected="true"</#if> value="HTML">HTML</option>
            <#else>
                <option <#if (doc.latestContent.format!'') == 'FTL'>selected="true"</#if> value="FTL">FTL</option>
                <option <#if (doc.latestContent.format!'') == 'ADOC'>selected="true"</#if> value="ADOC">ADOC</option>
                <option <#if (doc.latestContent.format!'') == 'HTML'>selected="true"</#if> value="HTML">HTML</option>
                <option <#if (doc.latestContent.format!'') == 'TEXT'>selected="true"</#if> value="TEXT">TEXT</option>
                <option <#if (doc.latestContent.format!'') == 'JSON'>selected="true"</#if> value="JSON">JSON</option>
            </#if>
        </select>
    </div>
</div>

<#if doc.docId?has_content>
<div class="form-group">
    <label for="latestContent.reasonForEdit" class="col-sm-2 control-label">Reason For Edit</label>
    <div class="col-sm-10">
        <input type="text" class="form-control" id="latestContent.reasonForEdit" name="latestContent.reasonForEdit" value="${doc.latestContent.reasonForEdit!''}">
    </div>
</div>
</#if>

<div class="form-group">
    <label for="latestContent.contentText" class="col-sm-2 control-label">Content</label>
    <div class="col-sm-10">
        <textarea class="form-control" id="latestContent.contentText" name="latestContent.contentText" rows="10" cols="80">${doc.latestContent.contentText!''}</textarea>
    </div>
</div>

<div class="form-group">
    <div class="col-sm-offset-2 col-sm-10">
        <button class="btn btn-success" name="btnAction" value="save">Save</button>
        <button class="btn btn-success" name="btnAction" value="publish">Publish</button>
    </div>
</div>

<script>
    CodeMirror.fromTextArea(document.getElementById("contentText"), {
        lineNumbers: true,
        lineWrapping: true
    });
</script>