from rest_framework import viewsets
from rest_framework.response import Response
import logging
from rest_framework.authentication import BasicAuthentication ,TokenAuthentication
from onadata.libs.mixins.object_lookup_mixin import ObjectLookupMixin
from onadata.libs.serializers.user_profile_serializer import (
    UserProfileWithTokenSerializer,UserSerializer)
#from onadata.libs.serializers.user_serializer import UserSerializer
from django.views.decorators.csrf import csrf_exempt
from django.views.decorators.http import  require_POST
from rest_framework.authtoken.models import Token
import json

class LoginViewSet(viewsets.ViewSet):

    authentication_classes = (BasicAuthentication,TokenAuthentication)
    #serializer_class = UserProfileWithTokenSerializer
    #serializer_class = UserSerializer

    @csrf_exempt
    def create(self, request, *args, **kwargs):


        return Response(UserSerializer(instance=request.user,context={"request": request}).data)

    """
    @csrf_exempt
    def list(self, request, *args, **kwargs):

        serializer = UserProfileWithTokenSerializer(
            instance=request.user.profile,
            context={"request": request})
        print serializer.data
        return Response(serializer.data)
    """