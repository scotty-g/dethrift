struct Name {
    1: required string firstName;
    2: required string lastName;
}

struct User {
    1: optional i64 id;
    2: optional Name name;
    3: optional list<Name> names;
}
