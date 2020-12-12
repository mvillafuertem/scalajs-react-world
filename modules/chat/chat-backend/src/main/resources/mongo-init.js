let chat = db.getSiblingDB('chat');
chat.getCollection('users').save({
    "name": "Pepe",
    "email": "pepe@email.com"
});
