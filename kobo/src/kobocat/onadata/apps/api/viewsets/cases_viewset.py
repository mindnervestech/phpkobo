from rest_framework.viewsets import ModelViewSet
from onadata.apps.logger.models import Case
from onadata.libs.serializers.user_serializer import UserSerializer
from onadata.apps.api import permissions
from django.views.generic.edit import CreateView

class CaseViewSet(CreateView):

    model = Case

    def create(self, request, *args, **kwargs):
        request.user



