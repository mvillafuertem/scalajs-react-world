let databaseName = "chat"
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
let users = chat.getUsers()
print(users)
