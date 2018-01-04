<div class="form-group">
    <label for="password" class="col-sm-2 control-label">Password</label>
    <div class="col-sm-10">
        <input type="password" class="form-control" id="password" name="password" value="${user.password!''}">
    </div>
</div>
<div class="form-group">
    <label for="fullName" class="col-sm-2 control-label">Full Name</label>
    <div class="col-sm-10">
        <input type="text" class="form-control" id="fullName" name="fullName" value="${user.fullName!''}">
    </div>
</div>
<div class="form-group">
    <label for="admin" class="col-sm-2 control-label">Admin</label>
    <div class="col-sm-10">
        <div id="admin" class="radio">
            <label><input type="radio" name="admin" value="true" <#if (user.admin)>checked </#if>value="YES"> YES</label>
            <label><input type="radio" name="admin" value="false" <#if !(user.admin)>checked </#if>value="NO"> NO</label>
        </div>
    </div>
</div>
