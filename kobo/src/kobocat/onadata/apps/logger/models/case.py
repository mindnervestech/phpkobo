from django.db import models
from django.contrib.auth.models import User
from onadata.apps.logger.models import Instance


class Case(models.Model):
    note = models.TextField()
    user = models.ForeignKey(User, related_name='cases', null=True)
    instances = models.ManyToManyField(Instance)
    #instance =  ForeignKey(Instance, related_name='cases', null=True)
    date_created = models.DateTimeField(auto_now_add=True)
    date_modified = models.DateTimeField(auto_now=True)
    consultant = models.ForeignKey(User, related_name='mycases', null=True)
    status = models.TextField(null=True)
    case_id = models.TextField()
    class Meta:
        app_label = 'logger'

