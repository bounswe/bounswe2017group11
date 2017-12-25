# -*- coding: utf-8 -*-
# Generated by Django 1.11.5 on 2017-12-20 20:42
from __future__ import unicode_literals

from django.db import migrations, models
import django.utils.timezone


class Migration(migrations.Migration):

    dependencies = [
        ('base', '0019_remove_annotation_item'),
    ]

    operations = [
        migrations.AddField(
            model_name='reported',
            name='created_at',
            field=models.DateTimeField(auto_now_add=True, default=django.utils.timezone.now),
            preserve_default=False,
        ),
    ]