databaseChangeLog {
  changeSet(id: '1483404843712-1', author: 'gregfarris (generated)') {
    createTable(tableName: 'AppRole') {
      column(name: 'ID', type: 'INT', autoIncrement: true) {
        constraints(primaryKey: true)
      }
      column(name: 'AdminName', type: 'VARCHAR(255)') {
        constraints(nullable: false)
      }
      column(name: 'Description', type: 'VARCHAR(255)') {
        constraints(nullable: false)
      }
    }
  }

  changeSet(id: '1483404843712-2', author: 'gregfarris (generated)') {
    createTable(tableName: 'AppRole_AppUser') {
      column(name: 'ID', type: 'INT', autoIncrement: true) {
        constraints(primaryKey: true)
      }
      column(name: 'AppRoleId', type: 'INT', defaultValueNumeric: 0) {
        constraints(nullable: false)
      }
      column(name: 'AppUserId', type: 'INT', defaultValueNumeric: 0) {
        constraints(nullable: false)
      }
    }
  }

  changeSet(id: '1483404843712-3', author: 'gregfarris (generated)') {
    createTable(tableName: 'AppUser') {
      column(name: 'ID', type: 'INT', autoIncrement: true) {
        constraints(primaryKey: true)
      }
      column(name: 'FacilityID', type: 'INT', defaultValueNumeric: 0) {
        constraints(nullable: false)
      }
      column(name: 'Login', type: 'VARCHAR(255)') {
        constraints(nullable: false)
      }
      column(name: 'Password', type: 'VARCHAR(255)') {
        constraints(nullable: false)
      }
      column(name: 'Valid', type: 'TINYINT') {
        constraints(nullable: false)
      }
      column(name: 'Launch', type: 'DATETIME') {
        constraints(nullable: false)
      }
      column(name: 'Expiration', type: 'DATETIME', defaultValue: '1000-01-01 00:00:00.000000') {
        constraints(nullable: false)
      }
      column(name: 'LastLoggedIn', type: 'DATETIME') {
        constraints(nullable: false)
      }
      column(name: 'LastActivity', type: 'DATETIME') {
        constraints(nullable: false)
      }
      column(name: 'LastRemoteAddr', type: 'VARCHAR(255)') {
        constraints(nullable: false)
      }
    }
  }

  changeSet(id: '1483404843712-4', author: 'gregfarris (generated)') {
    createTable(tableName: 'AppUserSetting') {
      column(name: 'AppUserSettingID', type: 'INT', autoIncrement: true) {
        constraints(primaryKey: true)
      }
      column(name: 'AppUserID', type: 'INT') {
        constraints(nullable: false)
      }
      column(name: 'AppUserSettingName', type: 'VARCHAR(255)') {
        constraints(nullable: false)
      }
      column(name: 'AppUserSettingValue', type: 'VARCHAR(255)') {
        constraints(nullable: false)
      }
    }
  }

  changeSet(id: '1483404843712-5', author: 'gregfarris (generated)') {
    createTable(tableName: 'Facility') {
      column(name: 'ID', type: 'INT', autoIncrement: true) {
        constraints(primaryKey: true)
      }
      column(name: 'FacilityName', type: 'VARCHAR(255)') {
        constraints(nullable: false)
      }
      column(name: 'FacilityShortcut', type: 'VARCHAR(50)') {
        constraints(nullable: false)
      }
      column(name: 'FacilityEmail', type: 'VARCHAR(50)') {
        constraints(nullable: false)
      }
      column(name: 'AdministratorID', type: 'INT', defaultValueNumeric: 0) {
        constraints(nullable: false)
      }
      column(name: 'Valid', type: 'TINYINT') {
        constraints(nullable: false)
      }
      column(name: 'Launch', type: 'DATETIME', defaultValue: '1000-01-01 00:00:00.000000') {
        constraints(nullable: false)
      }
      column(name: 'Expiration', type: 'DATETIME', defaultValue: '1000-01-01 00:00:00.000000') {
        constraints(nullable: false)
      }
    }
  }

  changeSet(id: '1483404843712-6', author: 'gregfarris (generated)') {
    createTable(tableName: 'FacilitySetting') {
      column(name: 'FacilitySettingID', type: 'INT', autoIncrement: true) {
        constraints(primaryKey: true)
      }
      column(name: 'FacilityID', type: 'INT') {
        constraints(nullable: false)
      }
      column(name: 'FacilitySettingName', type: 'VARCHAR(255)') {
        constraints(nullable: false)
      }
      column(name: 'FacilitySettingValue', type: 'VARCHAR(255)') {
        constraints(nullable: false)
      }
    }
  }

  changeSet(id: '1483404843712-7', author: 'gregfarris (generated)') {
    createTable(tableName: 'Facility_IP') {
      column(name: 'ip_from', type: 'INT UNSIGNED', defaultValueNumeric: 0) {
        constraints(nullable: false)
      }
      column(name: 'ip_to', type: 'INT UNSIGNED', defaultValueNumeric: 0) {
        constraints(nullable: false)
      }
      column(name: 'FacilityID', type: 'INT', defaultValueNumeric: 0) {
        constraints(nullable: false)
      }
    }
  }

  changeSet(id: '1483404843712-8', author: 'gregfarris (generated)') {
    createTable(tableName: 'HILOSEQUENCES') {
      column(name: 'SEQUENCENAME', type: 'VARCHAR(50)') {
        constraints(nullable: false)
      }
      column(name: 'HIGHVALUES', type: 'INT') {
        constraints(nullable: false)
      }
    }
  }

  changeSet(id: '1483404843712-9', author: 'gregfarris (generated)') {
    createTable(tableName: 'Lab') {
      column(name: 'ID', type: 'INT', autoIncrement: true) {
        constraints(primaryKey: true)
      }
      column(name: 'PatientID', type: 'INT') {
        constraints(nullable: false)
      }
      column(name: 'LabTestID', type: 'INT') {
        constraints(nullable: false)
      }
      column(name: 'LabDate', type: 'DATETIME', defaultValue: '1000-01-01 00:00:00.000000') {
        constraints(nullable: false)
      }
      column(name: 'LabText', type: 'TEXT') {
        constraints(nullable: false)
      }
    }
  }

  changeSet(id: '1483404843712-10', author: 'gregfarris (generated)') {
    createTable(tableName: 'LabTest') {
      column(name: 'ID', type: 'INT', autoIncrement: true) {
        constraints(primaryKey: true)
      }
      column(name: 'LabTestName', type: 'VARCHAR(255)') {
        constraints(nullable: false)
      }
      column(name: 'Valid', type: 'TINYINT') {
        constraints(nullable: false)
      }
      column(name: 'StartDate', type: 'DATETIME', defaultValue: '1000-01-01 00:00:00.000000') {
        constraints(nullable: false)
      }
      column(name: 'DiscontinueDate', type: 'DATETIME', defaultValue: '1000-01-01 00:00:00.000000') {
        constraints(nullable: false)
      }
    }
  }

  changeSet(id: '1483404843712-11', author: 'gregfarris (generated)') {
    createTable(tableName: 'LabTestDetail') {
      column(name: 'ID', type: 'INT', autoIncrement: true) {
        constraints(primaryKey: true)
      }
      column(name: 'LabTestID', type: 'INT') {
        constraints(nullable: false)
      }
      column(name: 'LabTestDetailName', type: 'VARCHAR(255)') {
        constraints(nullable: false)
      }
      column(name: 'LabTestDetailValue', type: 'TEXT') {
        constraints(nullable: false)
      }
    }
  }

  changeSet(id: '1483404843712-12', author: 'gregfarris (generated)') {
    createTable(tableName: 'Medications') {
      column(name: 'MedicationId', type: 'INT UNSIGNED', autoIncrement: true) {
        constraints(primaryKey: true)
      }
      column(name: 'RcopiaId', type: 'INT UNSIGNED') {
        constraints(nullable: false)
      }
      column(name: 'PrimaryFlag', type: 'VARCHAR(1)', defaultValue: 'Y')
      column(name: 'LastModifiedDate', type: 'DATETIME')
      column(name: 'DeletedFlag', type: 'VARCHAR(1)') {
        constraints(nullable: false)
      }
      column(name: 'PatientId', type: 'INT UNSIGNED') {
        constraints(nullable: false)
      }
      column(name: 'ProviderId', type: 'VARCHAR(20)')
      column(name: 'PreparerId', type: 'VARCHAR(20)')
      column(name: 'BrandName', type: 'VARCHAR(100)')
      column(name: 'GenericName', type: 'VARCHAR(100)') {
        constraints(nullable: false)
      }
      column(name: 'DrugRoute', type: 'VARCHAR(30)')
      column(name: 'DrugForm', type: 'VARCHAR(40)')
      column(name: 'DrugStrength', type: 'VARCHAR(100)')
      column(name: 'Action', type: 'VARCHAR(30)')
      column(name: 'DoseAmount', type: 'VARCHAR(30)')
      column(name: 'DoseUnit', type: 'VARCHAR(30)')
      column(name: 'DoseRoute', type: 'VARCHAR(30)')
      column(name: 'DoseTiming', type: 'VARCHAR(100)')
      column(name: 'DoseOther', type: 'VARCHAR(100)')
      column(name: 'Refills', type: 'VARCHAR(20)')
      column(name: 'Duration', type: 'BIGINT UNSIGNED')
      column(name: 'Quantity', type: 'DECIMAL(11, 3)')
      column(name: 'QuantityUnit', type: 'VARCHAR(40)')
      column(name: 'StartDate', type: 'DATETIME')
      column(name: 'Stopdate', type: 'DATETIME')
      column(name: 'PrescriptionRcopiaId', type: 'VARCHAR(20)')
    }
  }

  changeSet(id: '1483404843712-13', author: 'gregfarris (generated)') {
    createTable(tableName: 'Patient') {
      column(name: 'ID', type: 'INT', autoIncrement: true) {
        constraints(primaryKey: true)
      }
      column(name: 'FacilityID', type: 'INT') {
        constraints(nullable: false)
      }
      column(name: 'Valid', type: 'TINYINT') {
        constraints(nullable: false)
      }
      column(name: 'StartDate', type: 'DATETIME', defaultValue: '1000-01-01 00:00:00.000000') {
        constraints(nullable: false)
      }
      column(name: 'RcopiaLastUpdatedDate', type: 'DATETIME')
    }
  }

  changeSet(id: '1483404843712-14', author: 'gregfarris (generated)') {
    createTable(tableName: 'PatientDetail') {
      column(name: 'ID', type: 'INT', autoIncrement: true) {
        constraints(primaryKey: true)
      }
      column(name: 'PatientID', type: 'INT') {
        constraints(nullable: false)
      }
      column(name: 'PatientDetailName', type: 'VARCHAR(255)') {
        constraints(nullable: false)
      }
      column(name: 'PatientDetailValue', type: 'VARCHAR(255)') {
        constraints(nullable: false)
      }
      column(name: 'EntryDate', type: 'DATETIME', defaultValue: '1000-01-01 00:00:00.000000') {
        constraints(nullable: false)
      }
    }
  }

  changeSet(id: '1483404843712-15', author: 'gregfarris (generated)') {
    createTable(tableName: 'PatientStatus') {
      column(name: 'ID', type: 'INT', autoIncrement: true) {
        constraints(primaryKey: true)
      }
      column(name: 'PatientID', type: 'INT') {
        constraints(nullable: false)
      }
      column(name: 'PatientStatusName', type: 'VARCHAR(255)') {
        constraints(nullable: false)
      }
      column(name: 'PatientStatusValue', type: 'TEXT') {
        constraints(nullable: false)
      }
      column(name: 'EntryDate', type: 'DATETIME', defaultValue: '1000-01-01 00:00:00.000000') {
        constraints(nullable: false)
      }
    }
  }

  changeSet(id: '1483404843712-16', author: 'gregfarris (generated)') {
    createTable(tableName: 'PrescriptionEvent') {
      column(name: 'ID', type: 'INT', autoIncrement: true) {
        constraints(primaryKey: true)
      }
      column(name: 'PatientID', type: 'INT') {
        constraints(nullable: false)
      }
      column(name: 'TreatmentID', type: 'INT') {
        constraints(nullable: false)
      }
      column(name: 'RcopiaId', type: 'INT UNSIGNED') {
        constraints(nullable: false)
      }
      column(name: 'EntryDate', type: 'DATETIME') {
        constraints(nullable: false)
      }
      column(name: 'Duration', type: 'INT') {
        constraints(nullable: false)
      }
      column(name: 'Discontinue', type: 'TINYINT') {
        constraints(nullable: false)
      }
      column(name: 'DailyDose', type: 'VARCHAR(255)') {
        constraints(nullable: false)
      }
      column(name: 'DoctorName', type: 'VARCHAR(255)') {
        constraints(nullable: false)
      }
    }
  }

  changeSet(id: '1483404843712-17', author: 'gregfarris (generated)') {
    createTable(tableName: 'ProgressNote') {
      column(name: 'ID', type: 'INT', autoIncrement: true) {
        constraints(primaryKey: true)
      }
      column(name: 'PatientID', type: 'INT', defaultValueNumeric: 0) {
        constraints(nullable: false)
      }
      column(name: 'AppUserID', type: 'INT', defaultValueNumeric: 0) {
        constraints(nullable: false)
      }
      column(name: 'EntryDate', type: 'DATETIME', defaultValue: '1000-01-01 00:00:00.000000') {
        constraints(nullable: false)
      }
      column(name: 'DoctorName', type: 'VARCHAR(255)') {
        constraints(nullable: false)
      }
      column(name: 'NoteText', type: 'TEXT') {
        constraints(nullable: false)
      }
    }
  }

  changeSet(id: '1483404843712-18', author: 'gregfarris (generated)') {
    createTable(tableName: 'ProgressNoteTag') {
      column(name: 'ID', type: 'INT', autoIncrement: true) {
        constraints(primaryKey: true)
      }
      column(name: 'Valid', type: 'TINYINT') {
        constraints(nullable: false)
      }
      column(name: 'TagName', type: 'VARCHAR(255)') {
        constraints(nullable: false)
      }
      column(name: 'TagDescription', type: 'VARCHAR(255)') {
        constraints(nullable: false)
      }
    }
  }

  changeSet(id: '1483404843712-19', author: 'gregfarris (generated)') {
    createTable(tableName: 'ProgressNote_ProgressNoteTag') {
      column(name: 'ID', type: 'INT', autoIncrement: true) {
        constraints(primaryKey: true)
      }
      column(name: 'ProgressNoteId', type: 'INT', defaultValueNumeric: 0) {
        constraints(nullable: false)
      }
      column(name: 'ProgressNoteTagId', type: 'INT', defaultValueNumeric: 0) {
        constraints(nullable: false)
      }
    }
  }

  changeSet(id: '1483404843712-20', author: 'gregfarris (generated)') {
    createTable(tableName: 'RecommendAppRole') {
      column(name: 'ID', type: 'INT', autoIncrement: true) {
        constraints(primaryKey: true)
      }
      column(name: 'AdminName', type: 'VARCHAR(255)') {
        constraints(nullable: false)
      }
      column(name: 'Description', type: 'VARCHAR(255)') {
        constraints(nullable: false)
      }
    }
  }

  changeSet(id: '1483404843712-21', author: 'gregfarris (generated)') {
    createTable(tableName: 'RecommendAppRole_RecommendAppUser') {
      column(name: 'ID', type: 'INT', autoIncrement: true) {
        constraints(primaryKey: true)
      }
      column(name: 'RecommendAppRoleId', type: 'INT', defaultValueNumeric: 0) {
        constraints(nullable: false)
      }
      column(name: 'RecommendAppUserId', type: 'INT', defaultValueNumeric: 0) {
        constraints(nullable: false)
      }
    }
  }

  changeSet(id: '1483404843712-22', author: 'gregfarris (generated)') {
    createTable(tableName: 'RecommendAppUser') {
      column(name: 'ID', type: 'INT', autoIncrement: true) {
        constraints(primaryKey: true)
      }
      column(name: 'Login', type: 'VARCHAR(255)') {
        constraints(nullable: false)
      }
      column(name: 'Password', type: 'VARCHAR(255)') {
        constraints(nullable: false)
      }
      column(name: 'Valid', type: 'TINYINT') {
        constraints(nullable: false)
      }
      column(name: 'Launch', type: 'DATETIME') {
        constraints(nullable: false)
      }
      column(name: 'Expiration', type: 'DATETIME', defaultValue: '1000-01-01 00:00:00.000000') {
        constraints(nullable: false)
      }
      column(name: 'LastLoggedIn', type: 'DATETIME') {
        constraints(nullable: false)
      }
      column(name: 'LastActivity', type: 'DATETIME') {
        constraints(nullable: false)
      }
      column(name: 'LastRemoteAddr', type: 'VARCHAR(255)') {
        constraints(nullable: false)
      }
    }
  }

  changeSet(id: '1483404843712-23', author: 'gregfarris (generated)') {
    createTable(tableName: 'RecommendAppUserSetting') {
      column(name: 'RecommendAppUserSettingID', type: 'INT', autoIncrement: true) {
        constraints(primaryKey: true)
      }
      column(name: 'RecommendAppUserID', type: 'INT') {
        constraints(nullable: false)
      }
      column(name: 'RecommendAppUserSettingName', type: 'VARCHAR(255)') {
        constraints(nullable: false)
      }
      column(name: 'RecommendAppUserSettingValue', type: 'VARCHAR(255)') {
        constraints(nullable: false)
      }
    }
  }

  changeSet(id: '1483404843712-24', author: 'gregfarris (generated)') {
    createTable(tableName: 'RecommendDiagnosis') {
      column(name: 'DiagnosisID', type: 'INT', autoIncrement: true) {
        constraints(primaryKey: true)
      }
      column(name: 'Diagnosis', type: 'VARCHAR(250)') {
        constraints(nullable: false)
      }
      column(name: 'Stage', type: 'VARCHAR(5)') {
        constraints(nullable: false)
      }
      column(name: 'Notes', type: 'TEXT') {
        constraints(nullable: false)
      }
    }
  }

  changeSet(id: '1483404843712-25', author: 'gregfarris (generated)') {
    createTable(tableName: 'RecommendFacility') {
      column(name: 'ID', type: 'INT', autoIncrement: true) {
        constraints(primaryKey: true)
      }
      column(name: 'RecommendFacilityName', type: 'VARCHAR(255)') {
        constraints(nullable: false)
      }
      column(name: 'RecommendFacilityIdentifier', type: 'VARCHAR(255)') {
        constraints(nullable: false)
      }
      column(name: 'StartDate', type: 'DATETIME', defaultValue: '1000-01-01 00:00:00.000000') {
        constraints(nullable: false)
      }
    }
  }

  changeSet(id: '1483404843712-26', author: 'gregfarris (generated)') {
    createTable(tableName: 'RecommendMessage') {
      column(name: 'MessageID', type: 'INT', autoIncrement: true) {
        constraints(primaryKey: true)
      }
      column(name: 'Priority', type: 'INT') {
        constraints(nullable: false)
      }
      column(name: 'MessageTag', type: 'VARCHAR(20)') {
        constraints(nullable: false)
      }
      column(name: 'Message', type: 'TEXT') {
        constraints(nullable: false)
      }
    }
  }

  changeSet(id: '1483404843712-27', author: 'gregfarris (generated)') {
    createTable(tableName: 'RecommendPatient') {
      column(name: 'ID', type: 'INT', autoIncrement: true) {
        constraints(primaryKey: true)
      }
      column(name: 'RecommendFacilityID', type: 'INT') {
        constraints(nullable: false)
      }
      column(name: 'RecommendPatientIdentifier', type: 'VARCHAR(255)') {
        constraints(nullable: false)
      }
      column(name: 'Valid', type: 'TINYINT') {
        constraints(nullable: false)
      }
      column(name: 'StartDate', type: 'DATETIME', defaultValue: '1000-01-01 00:00:00.000000') {
        constraints(nullable: false)
      }
    }
  }

  changeSet(id: '1483404843712-28', author: 'gregfarris (generated)') {
    createTable(tableName: 'RecommendPatientDetail') {
      column(name: 'ID', type: 'INT', autoIncrement: true) {
        constraints(primaryKey: true)
      }
      column(name: 'RecommendPatientID', type: 'INT') {
        constraints(nullable: false)
      }
      column(name: 'RecommendPatientDetailName', type: 'VARCHAR(255)') {
        constraints(nullable: false)
      }
      column(name: 'RecommendPatientDetailValue', type: 'VARCHAR(255)') {
        constraints(nullable: false)
      }
      column(name: 'EntryDate', type: 'DATETIME', defaultValue: '1000-01-01 00:00:00.000000') {
        constraints(nullable: false)
      }
    }
  }

  changeSet(id: '1483404843712-29', author: 'gregfarris (generated)') {
    createTable(tableName: 'RecommendPatientStatus') {
      column(name: 'ID', type: 'INT', autoIncrement: true) {
        constraints(primaryKey: true)
      }
      column(name: 'RecommendPatientID', type: 'INT') {
        constraints(nullable: false)
      }
      column(name: 'RecommendPatientStatusName', type: 'VARCHAR(255)') {
        constraints(nullable: false)
      }
      column(name: 'RecommendPatientStatusValue', type: 'VARCHAR(255)') {
        constraints(nullable: false)
      }
      column(name: 'EntryDate', type: 'DATETIME', defaultValue: '1000-01-01 00:00:00.000000') {
        constraints(nullable: false)
      }
    }
  }

  changeSet(id: '1483404843712-30', author: 'gregfarris (generated)') {
    createTable(tableName: 'RecommendPrescriptionEvent') {
      column(name: 'ID', type: 'INT', autoIncrement: true) {
        constraints(primaryKey: true)
      }
      column(name: 'RecommendTreatmentID', type: 'INT') {
        constraints(nullable: false)
      }
      column(name: 'RecommendPatientID', type: 'INT') {
        constraints(nullable: false)
      }
      column(name: 'EntryDate', type: 'DATETIME') {
        constraints(nullable: false)
      }
      column(name: 'Duration', type: 'INT') {
        constraints(nullable: false)
      }
      column(name: 'Discontinue', type: 'TINYINT') {
        constraints(nullable: false)
      }
      column(name: 'DailyDose', type: 'VARCHAR(255)') {
        constraints(nullable: false)
      }
      column(name: 'DoctorName', type: 'VARCHAR(255)') {
        constraints(nullable: false)
      }
    }
  }

  changeSet(id: '1483404843712-31', author: 'gregfarris (generated)') {
    createTable(tableName: 'RecommendReason') {
      column(name: 'ID', type: 'INT', autoIncrement: true) {
        constraints(primaryKey: true)
      }
      column(name: 'PatientID', type: 'INT') {
        constraints(nullable: false)
      }
      column(name: 'ReasonDate', type: 'DATETIME') {
        constraints(nullable: false)
      }
      column(name: 'Reason', type: 'VARCHAR(255)') {
        constraints(nullable: false)
      }
      column(name: 'DoctorName', type: 'VARCHAR(255)') {
        constraints(nullable: false)
      }
    }
  }

  changeSet(id: '1483404843712-32', author: 'gregfarris (generated)') {
    createTable(tableName: 'RecommendRule') {
      column(name: 'RuleID', type: 'INT', autoIncrement: true) {
        constraints(primaryKey: true)
      }
      column(name: 'Priority', type: 'INT') {
        constraints(nullable: false)
      }
      column(name: 'Valid', type: 'INT') {
        constraints(nullable: false)
      }
      column(name: 'TrueMessageID', type: 'INT', defaultValueNumeric: 0) {
        constraints(nullable: false)
      }
      column(name: 'FalseMessageID', type: 'INT', defaultValueNumeric: 0) {
        constraints(nullable: false)
      }
      column(name: 'RuleType', type: 'VARCHAR(50)') {
        constraints(nullable: false)
      }
      column(name: 'RuleName', type: 'VARCHAR(50)') {
        constraints(nullable: false)
      }
    }
  }

  changeSet(id: '1483404843712-33', author: 'gregfarris (generated)') {
    createTable(tableName: 'RecommendRuleCriterium') {
      column(name: 'RuleCriteriumID', type: 'INT', autoIncrement: true) {
        constraints(primaryKey: true)
      }
      column(name: 'RuleID', type: 'INT') {
        constraints(nullable: false)
      }
      column(name: 'Priority', type: 'INT') {
        constraints(nullable: false)
      }
      column(name: 'Type', type: 'VARCHAR(50)') {
        constraints(nullable: false)
      }
      column(name: 'FieldName', type: 'VARCHAR(50)') {
        constraints(nullable: false)
      }
      column(name: 'Operator', type: 'VARCHAR(50)') {
        constraints(nullable: false)
      }
      column(name: 'Value', type: 'VARCHAR(50)') {
        constraints(nullable: false)
      }
    }
  }

  changeSet(id: '1483404843712-34', author: 'gregfarris (generated)') {
    createTable(tableName: 'RecommendRule_RecommendDiagnosis') {
      column(name: 'RuleID', type: 'INT') {
        constraints(nullable: false)
      }
      column(name: 'DiagnosisID', type: 'INT') {
        constraints(nullable: false)
      }
    }
  }

  changeSet(id: '1483404843712-35', author: 'gregfarris (generated)') {
    createTable(tableName: 'Sequence') {
      column(name: 'Entity', type: 'VARCHAR(255)') {
        constraints(nullable: false)
      }
      column(name: 'NextID', type: 'INT') {
        constraints(nullable: false)
      }
    }
  }

  changeSet(id: '1483404843712-36', author: 'gregfarris (generated)') {
    createTable(tableName: 'Treatment') {
      column(name: 'ID', type: 'INT', autoIncrement: true) {
        constraints(primaryKey: true)
      }
      column(name: 'TreatmentGroupID', type: 'INT') {
        constraints(nullable: false)
      }
      column(name: 'TreatmentName', type: 'VARCHAR(255)') {
        constraints(nullable: false)
      }
      column(name: 'TreatmentAbbr', type: 'VARCHAR(255)') {
        constraints(nullable: false)
      }
      column(name: 'Valid', type: 'TINYINT') {
        constraints(nullable: false)
      }
      column(name: 'DrfGenericDrugName', type: 'VARCHAR(255)')
    }
  }

  changeSet(id: '1483404843712-37', author: 'gregfarris (generated)') {
    createTable(tableName: 'TreatmentDetail') {
      column(name: 'ID', type: 'INT', autoIncrement: true) {
        constraints(primaryKey: true)
      }
      column(name: 'TreatmentID', type: 'INT') {
        constraints(nullable: false)
      }
      column(name: 'TreatmentDetailName', type: 'VARCHAR(255)') {
        constraints(nullable: false)
      }
      column(name: 'TreatmentDetailValue', type: 'TEXT') {
        constraints(nullable: false)
      }
    }
  }

  changeSet(id: '1483404843712-38', author: 'gregfarris (generated)') {
    createTable(tableName: 'TreatmentGroup') {
      column(name: 'ID', type: 'INT', autoIncrement: true) {
        constraints(primaryKey: true)
      }
      column(name: 'TreatmentGroupName', type: 'VARCHAR(255)') {
        constraints(nullable: false)
      }
      column(name: 'TreatmentGroupAbbr', type: 'VARCHAR(255)') {
        constraints(nullable: false)
      }
      column(name: 'Valid', type: 'TINYINT') {
        constraints(nullable: false)
      }
    }
  }

  changeSet(id: '1483404843712-39', author: 'gregfarris (generated)') {
    createTable(tableName: 'Treatment_Diagnosis') {
      column(name: 'ID', type: 'INT', autoIncrement: true) {
        constraints(primaryKey: true)
      }
      column(name: 'TreatmentID', type: 'INT') {
        constraints(nullable: false)
      }
      column(name: 'DiagnosisID', type: 'INT') {
        constraints(nullable: false)
      }
      column(name: 'Row', type: 'INT') {
        constraints(nullable: false)
      }
      column(name: 'Drug', type: 'INT') {
        constraints(nullable: false)
      }
    }
  }

  changeSet(id: '1483404843712-40', author: 'gregfarris (generated)') {
    createTable(tableName: 'content') {
      column(name: 'contentid', type: 'INT', autoIncrement: true) {
        constraints(primaryKey: true)
      }
      column(name: 'contenttypeid', type: 'INT', defaultValueNumeric: 0) {
        constraints(nullable: false)
      }
      column(name: 'contentorigpurpose', type: 'VARCHAR(50)') {
        constraints(nullable: false)
      }
      column(name: 'contentorigtable', type: 'VARCHAR(50)') {
        constraints(nullable: false)
      }
      column(name: 'contenttext', type: 'TEXT') {
        constraints(nullable: false)
      }
      column(name: 'createdate', type: 'DATETIME', defaultValue: '1000-01-01 00:00:00.000000') {
        constraints(nullable: false)
      }
      column(name: 'createuser', type: 'INT', defaultValueNumeric: 0) {
        constraints(nullable: false)
      }
    }
  }

  changeSet(id: '1483404843712-41', author: 'gregfarris (generated)') {
    createTable(tableName: 'contentjoin') {
      column(name: 'contentjoinid', type: 'INT', autoIncrement: true) {
        constraints(primaryKey: true)
      }
      column(name: 'contentid', type: 'INT', defaultValueNumeric: 0) {
        constraints(nullable: false)
      }
      column(name: 'contentjointable', type: 'VARCHAR(50)') {
        constraints(nullable: false)
      }
      column(name: 'contentjointablerowid', type: 'INT', defaultValueNumeric: 0) {
        constraints(nullable: false)
      }
      column(name: 'contentjoinpurpose', type: 'VARCHAR(50)') {
        constraints(nullable: false)
      }
      column(name: 'contentjoinpriority', type: 'INT', defaultValueNumeric: 0) {
        constraints(nullable: false)
      }
    }
  }

  changeSet(id: '1483404843712-42', author: 'gregfarris (generated)') {
    createTable(tableName: 'contenttype') {
      column(name: 'contenttypeid', type: 'INT', autoIncrement: true) {
        constraints(primaryKey: true)
      }
      column(name: 'contenttypemime', type: 'VARCHAR(16)') {
        constraints(nullable: false)
      }
      column(name: 'contenttypedescription', type: 'VARCHAR(255)') {
        constraints(nullable: false)
      }
    }
  }

  changeSet(id: '1483404843712-43', author: 'gregfarris (generated)') {
    addPrimaryKey(columnNames: 'ip_from, FacilityID', constraintName: 'PRIMARY', tableName: 'Facility_IP')
  }

  changeSet(id: '1483404843712-44', author: 'gregfarris (generated)') {
    addPrimaryKey(columnNames: 'SEQUENCENAME', constraintName: 'PRIMARY', tableName: 'HILOSEQUENCES')
  }

  changeSet(id: '1483404843712-45', author: 'gregfarris (generated)') {
    addPrimaryKey(columnNames: 'RuleID, DiagnosisID', constraintName: 'PRIMARY', tableName: 'RecommendRule_RecommendDiagnosis')
  }

  changeSet(id: '1483404843712-46', author: 'gregfarris (generated)') {
    addPrimaryKey(columnNames: 'Entity', constraintName: 'PRIMARY', tableName: 'Sequence')
  }

  changeSet(id: '1483404843712-47', author: 'gregfarris (generated)') {
    addUniqueConstraint(columnNames: 'PatientID, TreatmentID, RcopiaId, EntryDate', constraintName: 'PrescriptionEvent_UNIQUE', tableName: 'PrescriptionEvent')
  }

  changeSet(id: '1483404843712-48', author: 'gregfarris (generated)') {
    addUniqueConstraint(columnNames: 'RcopiaId, LastModifiedDate', constraintName: 'rcopiaLastModifiedUK', tableName: 'Medications')
  }

  changeSet(id: '1483404843712-49', author: 'gregfarris (generated)') {
    addUniqueConstraint(columnNames: 'RcopiaId, PrimaryFlag', constraintName: 'rcopiaPrimaryUK', tableName: 'Medications')
  }

  changeSet(id: '1483404843712-50', author: 'gregfarris (generated)') {
    createIndex(indexName: 'BrandNameIndex', tableName: 'Medications') {
      column(name: 'BrandName')
    }
  }

  changeSet(id: '1483404843712-51', author: 'gregfarris (generated)') {
    createIndex(indexName: 'PatientIdFK', tableName: 'Medications') {
      column(name: 'PatientId')
    }
  }

  changeSet(id: '1483404843712-52', author: 'gregfarris (generated)') {
    createIndex(indexName: 'contentjointable_rowid', tableName: 'contentjoin') {
      column(name: 'contentjointable')
      column(name: 'contentjointablerowid')
    }
  }

}
