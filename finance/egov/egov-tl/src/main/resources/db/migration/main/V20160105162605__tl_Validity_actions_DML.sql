Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Create-Validity','/validity/create',(select id from eg_module where name='Masters' and parentmodule=(select id from eg_module where name='Trade License' and parentmodule is null)),1,'Create-Validity',true,'tl',(select id from eg_module where name='Trade License' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Create-Validity'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Update-Validity','/validity/update',(select id from eg_module where name='Masters' and parentmodule=(select id from eg_module where name='Trade License' and parentmodule is null)),1,'Update-Validity',false,'tl',(select id from eg_module where name='Trade License' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Update-Validity'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'View-Validity','/validity/view',(select id from eg_module where name='Masters' and parentmodule=(select id from eg_module where name='Trade License' and parentmodule is null)),1,'View-Validity',false,'tl',(select id from eg_module where name='Trade License' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='View-Validity'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Edit-Validity','/validity/edit',(select id from eg_module where name='Masters' and parentmodule=(select id from eg_module where name='Trade License' and parentmodule is null)),1,'Edit-Validity',false,'tl',(select id from eg_module where name='Trade License' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Edit-Validity'));


Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Result-Validity','/validity/result',(select id from eg_module where name='Masters' and parentmodule=(select id from eg_module where name='Trade License' and parentmodule is null)),1,'Result-Validity',false,'tl',(select id from eg_module where name='Trade License' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Result-Validity'));
