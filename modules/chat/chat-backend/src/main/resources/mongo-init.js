let databaseName = "chat"
let collectionName = "users"
let chat = db.getSiblingDB(databaseName);
chat.createUser({
    user: "chat_user",
    pwd: "chat_password",
    roles: [
        { role: "userAdmin", db: databaseName },
        { role: "dbAdmin",   db: databaseName },
        { role: "readWrite", db: databaseName }
    ]
});
chat.getCollection(collectionName).save({
    "name": "Pepe",
    "email": "pepe@email.com"
});
let users = chat.getUsers()
print(users)
