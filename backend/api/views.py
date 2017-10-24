from django.http import HttpResponse, JsonResponse
from django.contrib.auth.models import User
from base.models import Item
from base.models import Profile
from base.serializers import ItemSerializer
from base.serializers import UserSerializer
from rest_framework.parsers import JSONParser
from rest_framework_jwt.settings import api_settings
from rest_framework import viewsets
from rest_framework import permissions
from rest_framework.decorators import api_view, permission_classes
from rest_framework.permissions import IsAuthenticated

import base64
from django.core.files.base import ContentFile

class ItemViewSet(viewsets.ModelViewSet):
	"""
    API endpoint that allows items to be viewed or edited.
    """
	queryset = Item.objects.all().order_by('-created_at')
	serializer_class = ItemSerializer
	permission_classes = (permissions.IsAuthenticated,)

	def perform_create(self, serializer):
		location = self.request.data.get('location');
		date = self.request.data.get('date');
		tags = self.request.data.get('tags');
		image = None
		if self.request.data.get('image'):
			image = self.request.data.get('image');
			if type(image) is str:
				format, imgstr = image.split(';base64,')
				ext = format.split('/')[-1]
				image = ContentFile(base64.b64decode(imgstr), name='item.' + ext) # You can save this as file instance.
		# serializer.save(featured_img=self.request.data.get('image'), created_by=self.request.user, date = date, location = location, tags = tags)
		serializer.save(featured_img=image, created_by=self.request.user, date = date, location = location, tags = tags)

def register(request):
	"""
    API endpoint that user can be registered.
    """
	if request.method == 'POST':
		data = JSONParser().parse(request)
		serializer = UserSerializer(data=data)
		if serializer.is_valid():
			user = serializer.save()
			jwt_payload_handler = api_settings.JWT_PAYLOAD_HANDLER
			jwt_encode_handler = api_settings.JWT_ENCODE_HANDLER
			payload = jwt_payload_handler(user)
			token = jwt_encode_handler(payload)
			data = serializer.data
			data['token'] = token
			return JsonResponse(data, status=201)
		return JsonResponse(serializer.errors, status=400)
	return HttpResponse("GET method not allowed")


def addLoc(request):
	"""
    API endpoint that a location can be created.
    """
	if request.method == 'POST':
		data = JSONParser().parse(request)
		serializer = LocationSerializer(data=data)
		if serializer.is_valid():
			location = serializer.save()
			return JsonResponse(data, status=201)
		return JsonResponse(serializer.errors, status=400)
	return HttpResponse("GET method not allowed")

@api_view(['GET'])
@permission_classes((IsAuthenticated, ))
def profile(request, id = ''):
	"""
    API endpoint that returns profile page.
    """
	if request.method == 'GET':
		if id:
			user = User.objects.get(pk=id)
		else:
			user = request.user
		response_data = {}
		response_data["username"] = user.username
		response_data["email"] = user.email
		if hasattr(user, 'profile'):
			response_data["fullName"] = user.profile.fullName
			response_data["birthday"] = user.profile.birthday
			response_data["location"] = user.profile.location
			try:
				response_data["photo"] = user.profile.photo.url
			except:
				response_data["photo"] = None
		return JsonResponse(response_data)
	return HttpResponse("POST method not allowed")

