<#import "/spring.ftl" as spring />
<h1>Edit arduino entity</h1>
<form action="/arduino/update" method="POST">
    Description:<@spring.formInput "arduino.description" />
    <@spring.showErrors "<br>" "text-danger" />
    <@spring.formHiddenInput "arduino.id"/>
    <input type="submit">
</form>

