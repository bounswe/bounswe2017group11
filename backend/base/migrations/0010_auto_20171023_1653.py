# -*- coding: utf-8 -*-
# Generated by Django 1.11.5 on 2017-10-23 13:53
from __future__ import unicode_literals

from django.db import migrations


class Migration(migrations.Migration):

    dependencies = [
        ('base', '0009_auto_20171023_1651'),
    ]

    operations = [
        migrations.RenameField(
            model_name='profile',
            old_name='nameSurname',
            new_name='fullName',
        ),
    ]