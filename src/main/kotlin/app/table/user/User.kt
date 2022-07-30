package app.table.user

data class User(val id: Int, val username: String, val age: Int) {
    constructor(name: String, age: Int) : this(-1, name, age) {}
}

