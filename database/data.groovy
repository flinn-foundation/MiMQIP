databaseChangeLog {

    changeSet(id: 'AppRoleLoad', author: 'greg') {
        loadData(tableName: 'AppRole', file: 'AppRole.csv') {
            column(name: 'AdminName', type: "STRING")
            column(name: 'Description', type: "STRING")
        }
    }

}

