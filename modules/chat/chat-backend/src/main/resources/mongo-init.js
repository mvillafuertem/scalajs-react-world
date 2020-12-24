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
chat.users.createIndex(
    {
        "email": 1
    },
    {
        unique: true,
        sparse: true,
        expireAfterSeconds: 3600
    }
)
let users = chat.getUsers()
print(users)
