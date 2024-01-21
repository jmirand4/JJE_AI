# JJE_AI
A Spring Boot microservices Application which detects objects from an image.

The application communicates with a CodeProject API to detect objects. There are three types of users: admin, who manages tasks (object detection in an image) and created users. An admin can ban/unban tasks and users, as well as edit user data. Normal users can create object detection tasks and check the tasks they have created, while premium users, similar to normal users, have the additional ability to modify their own data (name and password). If the API is unavailable, the task will not be completed and will be marked as "CANCELADA".

A API can be containerized using the provided docker-compose.yaml file. Need docker desktop to run it.


