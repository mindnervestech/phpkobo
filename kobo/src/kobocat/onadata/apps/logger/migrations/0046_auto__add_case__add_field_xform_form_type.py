# -*- coding: utf-8 -*-
from south.utils import datetime_utils as datetime
from south.db import db
from south.v2 import SchemaMigration
from django.db import models


class Migration(SchemaMigration):

    def forwards(self, orm):
        # Adding model 'Case'
        db.create_table(u'logger_case', (
            (u'id', self.gf('django.db.models.fields.AutoField')(primary_key=True)),
            ('note', self.gf('django.db.models.fields.TextField')()),
            ('user', self.gf('django.db.models.fields.related.ForeignKey')(related_name='cases', null=True, to=orm['auth.User'])),
            ('instance', self.gf('django.db.models.fields.related.ForeignKey')(related_name='cases', null=True, to=orm['logger.Instance'])),
            ('date_created', self.gf('django.db.models.fields.DateTimeField')(auto_now_add=True, blank=True)),
            ('date_modified', self.gf('django.db.models.fields.DateTimeField')(auto_now=True, blank=True)),
            ('consultant', self.gf('django.db.models.fields.related.ForeignKey')(related_name='mycases', null=True, to=orm['auth.User'])),
            ('status', self.gf('django.db.models.fields.TextField')(null=True)),
        ))
        db.send_create_signal('logger', ['Case'])

        # Adding field 'XForm.form_type'
        db.add_column(u'logger_xform', 'form_type',
                      self.gf('django.db.models.fields.IntegerField')(default=0),
                      keep_default=False)


    def backwards(self, orm):
        # Deleting model 'Case'
        db.delete_table(u'logger_case')

        # Deleting field 'XForm.form_type'
        db.delete_column(u'logger_xform', 'form_type')


    models = {
        u'auth.group': {
            'Meta': {'object_name': 'Group'},
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'name': ('django.db.models.fields.CharField', [], {'unique': 'True', 'max_length': '80'}),
            'permissions': ('django.db.models.fields.related.ManyToManyField', [], {'to': u"orm['auth.Permission']", 'symmetrical': 'False', 'blank': 'True'})
        },
        u'auth.permission': {
            'Meta': {'ordering': "(u'content_type__app_label', u'content_type__model', u'codename')", 'unique_together': "((u'content_type', u'codename'),)", 'object_name': 'Permission'},
            'codename': ('django.db.models.fields.CharField', [], {'max_length': '100'}),
            'content_type': ('django.db.models.fields.related.ForeignKey', [], {'to': u"orm['contenttypes.ContentType']"}),
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'name': ('django.db.models.fields.CharField', [], {'max_length': '50'})
        },
        u'auth.user': {
            'Meta': {'object_name': 'User'},
            'date_joined': ('django.db.models.fields.DateTimeField', [], {'default': 'datetime.datetime.now'}),
            'email': ('django.db.models.fields.EmailField', [], {'max_length': '75', 'blank': 'True'}),
            'first_name': ('django.db.models.fields.CharField', [], {'max_length': '30', 'blank': 'True'}),
            'groups': ('django.db.models.fields.related.ManyToManyField', [], {'symmetrical': 'False', 'related_name': "u'user_set'", 'blank': 'True', 'to': u"orm['auth.Group']"}),
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'is_active': ('django.db.models.fields.BooleanField', [], {'default': 'True'}),
            'is_staff': ('django.db.models.fields.BooleanField', [], {'default': 'False'}),
            'is_superuser': ('django.db.models.fields.BooleanField', [], {'default': 'False'}),
            'last_login': ('django.db.models.fields.DateTimeField', [], {'default': 'datetime.datetime.now'}),
            'last_name': ('django.db.models.fields.CharField', [], {'max_length': '30', 'blank': 'True'}),
            'password': ('django.db.models.fields.CharField', [], {'max_length': '128'}),
            'user_permissions': ('django.db.models.fields.related.ManyToManyField', [], {'symmetrical': 'False', 'related_name': "u'user_set'", 'blank': 'True', 'to': u"orm['auth.Permission']"}),
            'username': ('django.db.models.fields.CharField', [], {'unique': 'True', 'max_length': '30'})
        },
        u'contenttypes.contenttype': {
            'Meta': {'ordering': "('name',)", 'unique_together': "(('app_label', 'model'),)", 'object_name': 'ContentType', 'db_table': "'django_content_type'"},
            'app_label': ('django.db.models.fields.CharField', [], {'max_length': '100'}),
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'model': ('django.db.models.fields.CharField', [], {'max_length': '100'}),
            'name': ('django.db.models.fields.CharField', [], {'max_length': '100'})
        },
        'logger.attachment': {
            'Meta': {'object_name': 'Attachment'},
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'instance': ('django.db.models.fields.related.ForeignKey', [], {'related_name': "'attachments'", 'to': "orm['logger.Instance']"}),
            'media_file': ('django.db.models.fields.files.FileField', [], {'max_length': '100'}),
            'mimetype': ('django.db.models.fields.CharField', [], {'default': "''", 'max_length': '50', 'blank': 'True'})
        },
        'logger.case': {
            'Meta': {'object_name': 'Case'},
            'consultant': ('django.db.models.fields.related.ForeignKey', [], {'related_name': "'mycases'", 'null': 'True', 'to': u"orm['auth.User']"}),
            'date_created': ('django.db.models.fields.DateTimeField', [], {'auto_now_add': 'True', 'blank': 'True'}),
            'date_modified': ('django.db.models.fields.DateTimeField', [], {'auto_now': 'True', 'blank': 'True'}),
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'instance': ('django.db.models.fields.related.ForeignKey', [], {'related_name': "'cases'", 'null': 'True', 'to': "orm['logger.Instance']"}),
            'note': ('django.db.models.fields.TextField', [], {}),
            'status': ('django.db.models.fields.TextField', [], {'null': 'True'}),
            'user': ('django.db.models.fields.related.ForeignKey', [], {'related_name': "'cases'", 'null': 'True', 'to': u"orm['auth.User']"})
        },
        'logger.instance': {
            'Meta': {'object_name': 'Instance'},
            'date_created': ('django.db.models.fields.DateTimeField', [], {'auto_now_add': 'True', 'blank': 'True'}),
            'date_modified': ('django.db.models.fields.DateTimeField', [], {'auto_now': 'True', 'blank': 'True'}),
            'deleted_at': ('django.db.models.fields.DateTimeField', [], {'default': 'None', 'null': 'True'}),
            'geom': ('django.contrib.gis.db.models.fields.GeometryCollectionField', [], {'null': 'True'}),
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'json': ('jsonfield.fields.JSONField', [], {'default': '{}'}),
            'status': ('django.db.models.fields.CharField', [], {'default': "u'submitted_via_web'", 'max_length': '20'}),
            'survey_type': ('django.db.models.fields.related.ForeignKey', [], {'to': "orm['logger.SurveyType']"}),
            'user': ('django.db.models.fields.related.ForeignKey', [], {'related_name': "'instances'", 'null': 'True', 'to': u"orm['auth.User']"}),
            'uuid': ('django.db.models.fields.CharField', [], {'default': "u''", 'max_length': '249'}),
            'xform': ('django.db.models.fields.related.ForeignKey', [], {'related_name': "'instances'", 'null': 'True', 'to': "orm['logger.XForm']"}),
            'xml': ('django.db.models.fields.TextField', [], {})
        },
        'logger.instancehistory': {
            'Meta': {'object_name': 'InstanceHistory'},
            'date_created': ('django.db.models.fields.DateTimeField', [], {'auto_now_add': 'True', 'blank': 'True'}),
            'date_modified': ('django.db.models.fields.DateTimeField', [], {'auto_now': 'True', 'blank': 'True'}),
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'uuid': ('django.db.models.fields.CharField', [], {'default': "u''", 'max_length': '249'}),
            'xform_instance': ('django.db.models.fields.related.ForeignKey', [], {'related_name': "'submission_history'", 'to': "orm['logger.Instance']"}),
            'xml': ('django.db.models.fields.TextField', [], {})
        },
        'logger.note': {
            'Meta': {'object_name': 'Note'},
            'date_created': ('django.db.models.fields.DateTimeField', [], {'auto_now_add': 'True', 'blank': 'True'}),
            'date_modified': ('django.db.models.fields.DateTimeField', [], {'auto_now': 'True', 'blank': 'True'}),
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'instance': ('django.db.models.fields.related.ForeignKey', [], {'related_name': "'notes'", 'to': "orm['logger.Instance']"}),
            'note': ('django.db.models.fields.TextField', [], {})
        },
        'logger.surveytype': {
            'Meta': {'object_name': 'SurveyType'},
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'slug': ('django.db.models.fields.CharField', [], {'unique': 'True', 'max_length': '100'})
        },
        'logger.xform': {
            'Meta': {'ordering': "('id_string',)", 'unique_together': "(('user', 'id_string'), ('user', 'sms_id_string'))", 'object_name': 'XForm'},
            'allows_sms': ('django.db.models.fields.BooleanField', [], {'default': 'False'}),
            'bamboo_dataset': ('django.db.models.fields.CharField', [], {'default': "u''", 'max_length': '60'}),
            'date_created': ('django.db.models.fields.DateTimeField', [], {'auto_now_add': 'True', 'blank': 'True'}),
            'date_modified': ('django.db.models.fields.DateTimeField', [], {'auto_now': 'True', 'blank': 'True'}),
            'description': ('django.db.models.fields.TextField', [], {'default': "u''", 'null': 'True'}),
            'downloadable': ('django.db.models.fields.BooleanField', [], {'default': 'True'}),
            'encrypted': ('django.db.models.fields.BooleanField', [], {'default': 'False'}),
            'form_type': ('django.db.models.fields.IntegerField', [], {'default': '0'}),
            'has_start_time': ('django.db.models.fields.BooleanField', [], {'default': 'False'}),
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'id_string': ('django.db.models.fields.SlugField', [], {'max_length': '100'}),
            'instances_with_geopoints': ('django.db.models.fields.BooleanField', [], {'default': 'False'}),
            'json': ('django.db.models.fields.TextField', [], {'default': "u''"}),
            'last_submission_time': ('django.db.models.fields.DateTimeField', [], {'null': 'True', 'blank': 'True'}),
            'num_of_submissions': ('django.db.models.fields.IntegerField', [], {'default': '0'}),
            'require_auth': ('django.db.models.fields.BooleanField', [], {'default': 'False'}),
            'shared': ('django.db.models.fields.BooleanField', [], {'default': 'False'}),
            'shared_data': ('django.db.models.fields.BooleanField', [], {'default': 'False'}),
            'sms_id_string': ('django.db.models.fields.SlugField', [], {'default': "''", 'max_length': '100'}),
            'title': ('django.db.models.fields.CharField', [], {'max_length': '255'}),
            'user': ('django.db.models.fields.related.ForeignKey', [], {'related_name': "'xforms'", 'null': 'True', 'to': u"orm['auth.User']"}),
            'uuid': ('django.db.models.fields.CharField', [], {'default': "u''", 'max_length': '32'}),
            'xls': ('django.db.models.fields.files.FileField', [], {'max_length': '100', 'null': 'True'}),
            'xml': ('django.db.models.fields.TextField', [], {})
        },
        'logger.ziggyinstance': {
            'Meta': {'object_name': 'ZiggyInstance'},
            'client_version': ('django.db.models.fields.BigIntegerField', [], {'default': 'None', 'null': 'True'}),
            'date_created': ('django.db.models.fields.DateTimeField', [], {'auto_now_add': 'True', 'blank': 'True'}),
            'date_deleted': ('django.db.models.fields.DateTimeField', [], {'default': 'None', 'null': 'True'}),
            'date_modified': ('django.db.models.fields.DateTimeField', [], {'auto_now': 'True', 'blank': 'True'}),
            'entity_id': ('django.db.models.fields.CharField', [], {'max_length': '249'}),
            'form_instance': ('django.db.models.fields.TextField', [], {}),
            'form_version': ('django.db.models.fields.CharField', [], {'default': "u'1.0'", 'max_length': '10'}),
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'instance_id': ('django.db.models.fields.CharField', [], {'unique': 'True', 'max_length': '249'}),
            'reporter': ('django.db.models.fields.related.ForeignKey', [], {'related_name': "'ziggys'", 'to': u"orm['auth.User']"}),
            'server_version': ('django.db.models.fields.BigIntegerField', [], {}),
            'xform': ('django.db.models.fields.related.ForeignKey', [], {'related_name': "'ziggy_submissions'", 'null': 'True', 'to': "orm['logger.XForm']"})
        }
    }

    complete_apps = ['logger']