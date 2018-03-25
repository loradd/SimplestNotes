package it.univaq.f4i.iw.simplestnote.data.impl;

import com.mysql.jdbc.exceptions.MySQLTimeoutException;
import it.univaq.f4i.iw.framework.data.DataLayerException;
import it.univaq.f4i.iw.framework.data.DataLayerMysqlImpl;
import static it.univaq.f4i.iw.simplestnote.data.impl.SimplestNoteDataLayerMysqlImplQueries.*;
import static it.univaq.f4i.iw.simplestnote.data.impl.utilities.FileSystemManagement.delFileRecursively;
import static it.univaq.f4i.iw.simplestnote.data.impl.utilities.FileSystemManagement.levenshteinDistance;
import it.univaq.f4i.iw.simplestnote.data.model.Note;
import it.univaq.f4i.iw.simplestnote.data.model.SimplestNoteDataLayer;
import it.univaq.f4i.iw.simplestnote.data.model.Tag;
import it.univaq.f4i.iw.simplestnote.data.model.User;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 *
 * @author Lorenzo Addazi (addazi.lorenzo@gmail.com)
 */
public class SimplestNoteDataLayerMysqlImpl extends DataLayerMysqlImpl implements SimplestNoteDataLayer {

    /* SELECT */
    private PreparedStatement selectUserById; // get user instance from ID
    private PreparedStatement selectUserByIdForUpdate;
    private PreparedStatement selectNoteByIdAndVersion; // get specific version of a note for read purpose
    private PreparedStatement selectNoteByIdAndVersionForUpdate; // get specific version of a note for write purpose
    private PreparedStatement selectTagById; // get tag instance from ID
    private PreparedStatement selectTagByIdForUpdate;
    private PreparedStatement selectUsersByEmailAddress; // get all users with email_address containing given string
    private PreparedStatement selectUsersFromNote; // get all users participating to a note
    private PreparedStatement selectUsersFromNoteForUpdate;
    private PreparedStatement selectUsersFromNoteWithPermission;
    private PreparedStatement selectUsersFromNoteWithPermissionForUpdate;
    private PreparedStatement selectUsersFromNoteWithStatus;
    private PreparedStatement selectUsersFromNoteWithStatusForUpdate;
    private PreparedStatement selectUsersFromNoteWithPermissionAndStatus;
    private PreparedStatement selectUsersFromNoteWithPermissionAndStatusForUpdate;
    private PreparedStatement selectNotesByTitle; // get notes with title containing the given string (latest versions)
    private PreparedStatement selectNotesByTitleForUpdate;
    private PreparedStatement selectNotesFromUser; // get all notes related to a user
    private PreparedStatement selectNotesFromUserForUpdate;
    private PreparedStatement selectNotesFromUserWithPermission;
    private PreparedStatement selectNotesFromUserWithPermissionForUpdate;
    private PreparedStatement selectNotesFromUserWithStatus;
    private PreparedStatement selectNotesFromUserWithStatusForUpdate;
    private PreparedStatement selectNotesFromUserWithPermissionAndStatus;
    private PreparedStatement selectNotesFromUserWithPermissionAndStatusForUpdate;
    private PreparedStatement selectNotesFromTag; // get all notes related to a tag
    private PreparedStatement selectNotesFromTagForUpdate;
    private PreparedStatement selectNoteHistory; // get note history (all previous versions )
    private PreparedStatement selectNoteHistoryForUpdate;
    private PreparedStatement selectTagsByContent; // get tags containing given string
    private PreparedStatement selectTagsByContentForUpdate;
    private PreparedStatement selectTagsFromUser; // get tags used by a user
    private PreparedStatement selectTagsFromUserForUpdate;
    private PreparedStatement selectTagsFromNote; // get tags for a note
    private PreparedStatement selectTagsFromNoteForUpdate;
    private PreparedStatement selectTagInclusionsNumber;
    private PreparedStatement selectLatestNoteVersion; // get the latest version index of a note
    private PreparedStatement selectAllNoteVersions;

    /* INSERT */
    private PreparedStatement insertUser; // insert new user instance
    private PreparedStatement insertNoteStatic; // insert new note instance 
    private PreparedStatement insertNoteDynamic; // insert new note version instance
    private PreparedStatement insertTag; // insert new tag
    private PreparedStatement insertUserNote; // insert new user participation to note
    private PreparedStatement insertNoteTag; // insert new tag for a note
    /* UPDATE */
    private PreparedStatement updateUser; // update user instance
    private PreparedStatement updateNoteStatic; // update note instance
    private PreparedStatement updateNoteDynamic; // update note version instance
    private PreparedStatement updateTag; // update tag instance
    private PreparedStatement updateUserNote; // update user participation to role (permission or status)
    /* DELETE */
    private PreparedStatement deleteUser; // delete user instance (foreign key constraints before)
    private PreparedStatement deleteTag; // delete tag instance
    private PreparedStatement deleteNoteStatic; // delete note instance
    private PreparedStatement deleteNoteVersion; // delete note version
    private PreparedStatement deleteUserParticipationToNote; // delete user participation to note
    private PreparedStatement deleteAllUserParticipationsToNotes; // delete user participation to all notes
    private PreparedStatement deleteAllUsersParticipationToNote; // delete all users participations to a single note
    private PreparedStatement deleteSingleTagFromNoteVersion; // delete tag from note version
    private PreparedStatement deleteSingleTagFromAllNotesVersions; // delte tag from all versions of all notes
    private PreparedStatement deleteAllTagsFromNoteVersions; // delete all tags from all versions of a single note
    private PreparedStatement selectUserNoteParticipationPermission;
    private PreparedStatement selectUsersByEmailAddressForUpdate;

    public SimplestNoteDataLayerMysqlImpl(DataSource datasource) throws SQLException, NamingException {
        super(datasource);
    }

    @Override
    public void init() throws DataLayerException {
        
        try {
            super.init();
            selectUserById = connection.prepareStatement(selectUserByIdQuery);
            selectUserByIdForUpdate = connection.prepareStatement(selectUserByIdQuery + forUpdate);
            selectUserByIdForUpdate.setQueryTimeout(1);
            selectUsersByEmailAddress = connection.prepareStatement(selectUsersByEmailAddressQuery);
            selectUsersByEmailAddressForUpdate = connection.prepareStatement(selectUsersByEmailAddressQuery + forUpdate);
            selectUsersByEmailAddressForUpdate.setQueryTimeout(1);
            selectNoteByIdAndVersion = connection.prepareStatement(selectNoteByIdAndVersionQuery); 
            selectNoteByIdAndVersionForUpdate = connection.prepareStatement(selectNoteByIdAndVersionQuery + forUpdate);
            selectNoteByIdAndVersionForUpdate.setQueryTimeout(1);
            selectTagById = connection.prepareStatement(selectTagByIdQuery);
            selectTagByIdForUpdate = connection.prepareStatement(selectTagByIdQuery + forUpdate);
            selectTagByIdForUpdate.setQueryTimeout(1); 
            selectUsersFromNote = connection.prepareStatement(selectUsersFromNoteQuery);
            selectUsersFromNoteForUpdate = connection.prepareStatement(selectUsersFromNoteQuery + forUpdate);
            selectUsersFromNoteForUpdate.setQueryTimeout(1);
            selectUsersFromNoteWithStatus = connection.prepareStatement(selectUsersFromNoteQuery + andStatus);
            selectUsersFromNoteWithStatusForUpdate = connection.prepareStatement(selectUsersFromNoteQuery + andStatus + forUpdate);
            selectUsersFromNoteWithStatusForUpdate.setQueryTimeout(1);
            selectUsersFromNoteWithPermission = connection.prepareStatement(selectUsersFromNoteQuery + andPermission);
            selectUsersFromNoteWithPermissionForUpdate = connection.prepareStatement(selectUsersFromNoteQuery + andPermission + forUpdate);
            selectUsersFromNoteWithPermissionForUpdate.setQueryTimeout(1);
            selectUsersFromNoteWithPermissionAndStatus = connection.prepareStatement(selectUsersFromNoteQuery + andStatus + andPermission);
            selectUsersFromNoteWithPermissionAndStatusForUpdate = connection.prepareStatement(selectUsersFromNoteQuery + andStatus + andPermission + forUpdate);
            selectUsersFromNoteWithPermissionAndStatusForUpdate.setQueryTimeout(1);
            selectNotesByTitle = connection.prepareStatement(selectNotesByTitleQuery); //%title tested
            selectNotesByTitleForUpdate = connection.prepareStatement(selectNotesByTitleQuery + forUpdate);
            selectNotesByTitleForUpdate.setQueryTimeout(1);
            selectNotesFromUser = connection.prepareStatement(selectNotesFromUserQuery); // tested
            selectNotesFromUserForUpdate = connection.prepareStatement(selectNotesFromUserQuery + forUpdate);
            selectNotesFromUserWithPermission = connection.prepareStatement(selectNotesFromUserQuery + andPermission);
            selectNotesFromUserWithPermissionForUpdate = connection.prepareStatement(selectNotesFromUserQuery + andPermission + forUpdate);
            selectNotesFromUserWithStatus = connection.prepareStatement(selectNotesFromUserQuery + andStatus);
            selectNotesFromUserWithStatusForUpdate = connection.prepareStatement(selectNotesFromUserQuery + andStatus + forUpdate);
            selectNotesFromUserWithPermissionAndStatus = connection.prepareStatement(selectNotesFromUserQuery + andPermission + andStatus);
            selectNotesFromUserWithPermissionAndStatusForUpdate = connection.prepareStatement(selectNotesFromUserQuery + andPermission + andStatus + forUpdate);
            selectNotesFromTag = connection.prepareStatement(selectNotesFromTagQuery); // tested
            selectNotesFromTagForUpdate = connection.prepareStatement(selectNotesFromTagQuery + forUpdate);
            selectNoteHistory = connection.prepareStatement(selectNoteHistoryQuery); // tested
            selectNoteHistoryForUpdate = connection.prepareStatement(selectNoteHistoryQuery + forUpdate);
            selectTagsByContent = connection.prepareStatement(selectTagsByContentQuery); // %content tested
            selectTagsByContentForUpdate = connection.prepareStatement(selectTagsByContentQuery + forUpdate);
            selectTagsFromUser = connection.prepareStatement(selectTagsFromUserQuery); // tested
            selectTagsFromUserForUpdate = connection.prepareStatement(selectTagsFromUserQuery + forUpdate);
            selectTagsFromNote = connection.prepareStatement(selectTagsFromNoteQuery); // tested
            selectTagsFromNoteForUpdate = connection.prepareStatement(selectTagsFromNoteQuery + forUpdate);
            selectLatestNoteVersion = connection.prepareStatement(selectLatestNoteVersionQuery); // tested (is this really useful?!)
            selectAllNoteVersions = connection.prepareStatement(selectAllNotesVersionsQuery);
            selectUserNoteParticipationPermission = connection.prepareStatement(selectUserParticipationPermissionQuery);
            selectTagInclusionsNumber = connection.prepareStatement(selectTagInclusionsNumberQuery);
            /* INSERT */
            insertUser = connection.prepareStatement(insertUserQuery, Statement.RETURN_GENERATED_KEYS); // tested
            insertNoteStatic = connection.prepareStatement(insertNoteStaticQuery, Statement.RETURN_GENERATED_KEYS); // tested
            insertNoteDynamic = connection.prepareStatement(insertNoteDynamicQuery); // tested
            insertTag = connection.prepareStatement(insertTagQuery, Statement.RETURN_GENERATED_KEYS); // tested
            insertUserNote = connection.prepareStatement(insertUserNoteQuery); // tested
            insertNoteTag = connection.prepareStatement(insertNoteTagQuery); // tested
            /* UPDATE */
            updateUserNote = connection.prepareStatement(updateUserNoteQuery); // tested
            updateUser = connection.prepareStatement(updateUserQuery); // tested
            updateNoteStatic = connection.prepareStatement(updateNoteStaticQuery); // tested
            updateNoteDynamic = connection.prepareStatement(updateNoteDynamicQuery); // tested
            updateTag = connection.prepareStatement(updateTagQuery); // tested
            /* DELETE */
            deleteUser = connection.prepareStatement(deleteUserQuery); // tested
            deleteNoteStatic = connection.prepareStatement(deleteNoteStaticQuery); // tested
            deleteNoteVersion = connection.prepareStatement(deleteNoteDynamicQuery + withVersion); // tested
            deleteUserParticipationToNote = connection.prepareStatement(deleteUserParticipationToNoteQuery + withNote); // tested
            deleteAllUserParticipationsToNotes = connection.prepareStatement(deleteUserParticipationToNoteQuery); // tested
            deleteAllUsersParticipationToNote = connection.prepareStatement(deleteUsersParticipationsFromNoteQuery);
            deleteSingleTagFromNoteVersion = connection.prepareStatement(deleteTagFromNoteQuery + fromTag + withNoteId + withNoteVersion); // tested
            deleteSingleTagFromAllNotesVersions = connection.prepareStatement(deleteTagFromNoteQuery + fromTag); // tested
            deleteAllTagsFromNoteVersions = connection.prepareStatement(deleteTagFromNoteQuery + fromNote); // tested
            deleteTag = connection.prepareStatement(deleteTagQuery);
        } catch (SQLException ex) {
            /* Logger.getLogger(SimplestNoteDataLayerMysqlImpl.class.getName()).log(Level.SEVERE, null, ex); - DEBUG ONLY */
            throw new DataLayerException("An Unexpected Error Occurred! :(");
        }
    }

    @Override
    public User createUser() throws DataLayerException{
        return new UserMysqlImpl(this);
    }

    @Override
    public Note createNote() throws DataLayerException{
        return new NoteMysqlImpl(this);
    }

    @Override
    public Tag createTag() throws DataLayerException{
        return new TagMysqlImpl(this);
    }

    @Override
    public User getUserByKey(int key, int access_mode) throws DataLayerException {
        User result = null;
        ResultSet queryResultSet = null;
        try {
            try {
                if(key > 0){
                    if (access_mode == READ_ACCESS) {
                        selectUserById.setInt(1, key);
                        queryResultSet = selectUserById.executeQuery();
                    } else if (access_mode == WRITE_ACCESS) {
                        connection.setAutoCommit(false);
                        selectUserByIdForUpdate.setInt(1, key);
                        queryResultSet = selectUserByIdForUpdate.executeQuery();
                    } else {
                        throw new DataLayerException("An Unexpected Error Occurred! :(");
                        /* throw new DataLayerException("Wrong Access Mode : " + access_mode + " - SimplestNoteDataLayerMysqlImpl.getUserByKey(int key, int access_mode)"); - DEBUG ONLY */
                    }
                } else {
                    throw new DataLayerException("An Unexpected Error Occurred! :(");
                    /* throw new DataLayerException("Wrong User Key : " + key + " - SimplestNoteDataLayerMysqlImpl.getUserByKey(int key, int access_mode)"); - DEBUG ONLY */
                }
            } catch (MySQLTimeoutException ex) {
                access_mode = READ_ACCESS;
                connection.setAutoCommit(true);
                selectUserById.setInt(1, key);
                queryResultSet = selectUserById.executeQuery();
            }
            if (queryResultSet != null && queryResultSet.next()) {
                if (access_mode == READ_ACCESS || access_mode == WRITE_ACCESS) {
                    result = new UserMysqlImpl(this, queryResultSet, access_mode);
                } else {
                    throw new DataLayerException("An Unexpected Error Occurred! :(");
                    /* throw new DataLayerException("Wrong Access Mode : " + access_mode + " - SimplestNoteDataLayerMysqlImpl.getUserByKey(int key, int access_mode)"); - DEBUG ONLY */
                }
            }
        } catch (SQLException e) {
            throw new DataLayerException("An Unexpected Error Occurred! :(", e);
            /* throw new DataLayerException("SQLException thrown - SimplestNoteDataLayerMysqlImpl.getUserByKey(int key, int access_mode)", e); - DEBUG ONLY */
        } finally {
            try {
                if (queryResultSet != null) {
                    queryResultSet.close();
                }
            } catch (SQLException e) {/* Nothing Done*/}
        }
        return result;
    }

    @Override
    public Note getNoteByKey(int key, int version, int access_mode) throws DataLayerException {
        Note result = null;
        ResultSet queryResultSet = null;
        boolean version_available = false;
        try {
            if(key > 0){
                if (version == LATEST_VERSION) {
                    selectLatestNoteVersion.setInt(1, key);
                    queryResultSet = selectLatestNoteVersion.executeQuery();
                    if (queryResultSet.next()) {
                        version = queryResultSet.getInt("latest_version");
                        version_available = true;
                    }
                } else if (version == PREVIOUS_VERSION) {
                    selectLatestNoteVersion.setInt(1, key);
                    queryResultSet = selectLatestNoteVersion.executeQuery();
                    if (queryResultSet.next()) {
                        version = queryResultSet.getInt("latest_version");
                        if (version > 1) {
                            version -= 1;
                        }
                    }
                    selectAllNoteVersions.setInt(1, key);
                    queryResultSet = selectAllNoteVersions.executeQuery();
                    while (queryResultSet.next()) {
                        if (version == queryResultSet.getInt("version")) {
                            version_available = true;
                            break;
                        }
                    }
                } else {
                    selectAllNoteVersions.setInt(1, key);
                    queryResultSet = selectAllNoteVersions.executeQuery();
                    while (queryResultSet.next()) {
                        if (version == queryResultSet.getInt("version")) {
                            version_available = true;
                            break;
                        }
                    }
                }
                if (!version_available) {
                    throw new DataLayerException("An Unexpected Error Occurred! :(");
                    /* throw new DataLayerException("Unavailable Note Version : " + version + " - SimplestNoteDataLayerMysqlImpl.getNoteByKey(int key, int version, int access_mode)"); - DEBUG ONLY */
                }
                try {
                    if (access_mode == READ_ACCESS) {
                        selectNoteByIdAndVersion.setInt(1, key);
                        selectNoteByIdAndVersion.setInt(2, version);
                        queryResultSet = selectNoteByIdAndVersion.executeQuery();
                    } else if (access_mode == WRITE_ACCESS) {
                        connection.setAutoCommit(false);
                        selectNoteByIdAndVersionForUpdate.setInt(1, key);
                        selectNoteByIdAndVersionForUpdate.setInt(2, version);
                        queryResultSet = selectNoteByIdAndVersionForUpdate.executeQuery();
                    } else {
                        throw new DataLayerException("An Unexpected Error Occurred! :( ");
                        /* throw new DataLayerException("Wrong Access Mode : " + access_mode + " - SimplestNoteDataLayerMysqlImpl.getNoteByKey(int key, int version, int access_mode)"); - DEBUG ONLY */
                    }
                } catch (MySQLTimeoutException ex) {
                    access_mode = READ_ACCESS;
                    connection.setAutoCommit(true);
                    selectNoteByIdAndVersion.setInt(1, key);
                    selectNoteByIdAndVersion.setInt(2, version);
                    queryResultSet = selectNoteByIdAndVersion.executeQuery();
                }
                if (queryResultSet.next()) {
                    if (access_mode == READ_ACCESS || access_mode == WRITE_ACCESS) { 
                        result = new NoteMysqlImpl(this, queryResultSet, access_mode);
                        result.getCreatorUser(READ_ACCESS);
                        result.getReadOnlyUsers(READ_ACCESS);
                        result.getReadWriteUsers(READ_ACCESS);
                        result.getNoteTags(READ_ACCESS);
                        result.getPendingUsers(READ_ACCESS);
                    } else {
                        throw new DataLayerException("An Unexpected Error Occurred! :(");
                        /* throw new DataLayerException("Wrong Access Mode : " + access_mode + " - SimplestNoteDataLayerMysqlImpl.getNoteByKey(int key, int version, int access_mode)"); - DEBUG ONLY */
                    }
                }
            } else {
                throw new DataLayerException("An Unexpected Error Occurred! :(");
                /* throw new DataLayerException("Wrong Note Key : " + key + " - SimplestNoteDataLayerMysqlImpl.getNoteByKey(int key, int version, int access_mode)"); - DEBUG ONLY */
            }
        } catch (SQLException e) {
            throw new DataLayerException("An Unexpected Error Occurred! :(");
            /* throw new DataLayerException("SQLException - SimplestNoteDataLayerMysqlImpl.getNoteByKey(int key, int version, int access_mode", e); - DEBUG ONLY "); */
        } finally {
            try {
                if (queryResultSet != null) {
                    queryResultSet.close();
                }
            } catch (SQLException e) {/* Nothing Done */}
        }
        return result;
    }

    @Override
    public Tag getTagByKey(int key, int access_mode) throws DataLayerException {
        Tag result = null;
        ResultSet queryResultSet = null;
        try {
            try {
                if(key > 0){
                    if (access_mode == READ_ACCESS) {
                        selectTagById.setInt(1, key);
                        queryResultSet = selectTagById.executeQuery();
                    } else if (access_mode == WRITE_ACCESS) {
                        connection.setAutoCommit(false);
                        selectTagByIdForUpdate.setInt(1, key);
                        queryResultSet = selectTagByIdForUpdate.executeQuery();
                    } else {
                        throw new DataLayerException("An Unexpected Error Occurred! :(");
                        /* throw new DataLayerException("Wrong Access Mode : " + access_mode + " - SimplestNoteMysqlImpl.getTagByKey(int key, int access_mode)"); - DEBUG ONLY */
                    }
                } else {
                    throw new DataLayerException("An Unexpected Error Occurred! :(");
                    /* throw new DataLayerException("Wrong Tag Key :" + key + " - SimplestNoteDataLayerMysqlImpl.getTagByKey(int key, int access_mode)"); - DEBUG ONLY */
                }
            } catch (MySQLTimeoutException ex) {
                access_mode = READ_ACCESS;
                connection.setAutoCommit(true);
                selectTagById.setInt(1, key); // ID
                queryResultSet = selectTagById.executeQuery();
            }
            if (queryResultSet.next()) {
                if (access_mode == READ_ACCESS || access_mode == WRITE_ACCESS) {
                    result = new TagMysqlImpl(this, queryResultSet, access_mode);
                } else {
                    throw new DataLayerException("An Unexpected Error Occurred! :(");
                    /* throw new DataLayerException("Wrong Access Mode : " + access_mode + " - SimplestNoteMysqlImpl.getTagByKey(int key, int access_mode)"); - DEBUG ONLY */
                }
            }
        } catch (SQLException e) {
            throw new DataLayerException("An Unexpected Error Occurred! :(");
            /* throw new DataLayerException("SQLException - SimplestNoteMysqlImpl.getTagByKey(int key, int access_mode)", e); - DEBUG ONLY */
        } finally {
            try {
                if (queryResultSet != null) {
                    queryResultSet.close();
                }
            } catch (SQLException e) {/* Nothing Done*/}
        }
        return result;
    }

    @Override
    public List<User> getUsersFromEmailAddress(String email_address, int access_mode) throws DataLayerException {
        List<User> result = new ArrayList();
        ResultSet queryResultSet = null;
        try {
            try {
                if(null != email_address && !email_address.isEmpty()){ // verificare che una stringa vuota sia considerata come errore !!!
                    if (access_mode == READ_ACCESS) {
                        selectUsersByEmailAddress.setString(1, email_address + "%");
                        queryResultSet = selectUsersByEmailAddress.executeQuery();
                    } else if (access_mode == WRITE_ACCESS) {
                        connection.setAutoCommit(false);
                        selectUsersByEmailAddressForUpdate.setString(1, email_address + "%");
                        queryResultSet = selectUsersByEmailAddressForUpdate.executeQuery();
                    } else {
                        throw new DataLayerException("An Unexpected Error Occurred! :(");
                        /* throw new DataLayerException("Wrong Access Mode : " + access_mode + " - SimplestNoteMysqlImpl.getUserFromEmailAddress(String email_address, int access_mode)"); - DEBUG ONLY */
                    }
                } else {
                    throw new DataLayerException("An Unexpected Error Occurred! :(");
                    /* throw new DataLayerException("Wrong email_address String : " + (( null != email_address )? (email_address.isEmpty())? "Empty String Error" : email_address : "Null String Error") + " - SimplestNoteDataLayerMysqlImpl.getUsersFromEmailAddress(String email_address, int access_mode)"); - DEBUG ONLY */
                }
            } catch (MySQLTimeoutException ex) {
                access_mode = READ_ACCESS;
                connection.setAutoCommit(true);
                selectUsersByEmailAddress.setString(1, email_address + "%");
                queryResultSet = selectUsersByEmailAddress.executeQuery();
            }
            while (queryResultSet.next()) {
                if (access_mode == READ_ACCESS || access_mode == WRITE_ACCESS) {
                    result.add(getUserByKey(queryResultSet.getInt("id"), access_mode));
                } else {
                    throw new DataLayerException("An Unexpected Error Occurred! :(");
                    /* throw new DataLayerException("Wrong Access Mode : " + access_mode + " - SimplestNoteMysqlImpl.getUserFromEmailAddress(String email_address, int access_mode)"); - DEBUG ONLY */
                }
            }
        } catch (SQLException e) {
            throw new DataLayerException("An Unexpected Error Occurred! :(");
            /* throw new DataLayerException("SQLException - SimplestNoteMysqlImpl.getUserFromEmailAddress(String email_address, int access_mode)", e); - DEBUG ONLY */
        } finally {
            try {
                if (null != queryResultSet) {
                    queryResultSet.close();
                }
            } catch (SQLException e) {/*Nothing Done*/}
        }
        return result;
    }

    @Override
    public int getUserNotePermission(User user, Note note) throws DataLayerException {
        int result = 0;
        ResultSet queryResultSet = null;
        try {
            if(null != user){
                if(null != note){
                    selectUserNoteParticipationPermission.setInt(1, user.getId());
                    selectUserNoteParticipationPermission.setInt(2, note.getId());
                    queryResultSet = selectUserNoteParticipationPermission.executeQuery();
                    if(queryResultSet.next()){
                        result = queryResultSet.getInt("permission");
                    }
                } else {
                    throw new DataLayerException("An Unexpected Error Occurred! :(");
                    /* throw new DataLayerException("Null Note Instance Error - SimplestNoteDataLayerMysqlImpl.getUserNotePermission(User user, Note note)"); - DEBUG ONLY */
                }
            } else {
                throw new DataLayerException("An Unexpected Error Occurred! :(");
                /* throw new DataLayerException("Null User Instance Error - SimplestNoteDataLayerMysqlImpl.getUserNotePermission(User user, Note note)"); - DEBUG ONLY */
            }
        } catch (SQLException e) {
            throw new DataLayerException("An Unexpected Error Occurred! :(");
            /* throw new DataLayerException("SQLException - SimplestNoteDataLayerMysqlImpl.getUserNotePermission(User user, Note note)", e); - DEBUG ONLY */
        } finally {
            try {
                if (queryResultSet != null) {
                    queryResultSet.close();
                }
            } catch (SQLException e) {/* Nothing Done*/}
        }
        return result;
    }

    @Override
    public List<User> getUsersFromNote(Note note, int permission, int status, int access_mode) throws DataLayerException {
        List<User> result = new ArrayList();
        ResultSet queryResultSet = null;
        try {
            if (null == note) {
                throw new DataLayerException("An Unexpected Error Occurred! :(");
                /* throw new DataLayerException("Null Note Instance Error - SimplestNoteDataLayerMysqlImpl.getUsersFromNote(Note note, int permission, int status, int access_mode)"); - DEBUG ONLY */
            }
            if (permission == CREATOR_PERMISSION) {
                if (status == ALL_STATUS || status == CONFIRMED_STATUS) {
                    try {
                        if (access_mode == READ_ACCESS) {
                            selectUsersFromNoteWithPermissionAndStatus.setInt(1, note.getId());
                            selectUsersFromNoteWithPermissionAndStatus.setInt(2, CONFIRMED_STATUS);
                            selectUsersFromNoteWithPermissionAndStatus.setInt(3, permission);
                            queryResultSet = selectUsersFromNoteWithPermissionAndStatus.executeQuery();
                        } else if (access_mode == WRITE_ACCESS) {
                            connection.setAutoCommit(false);
                            selectUsersFromNoteWithPermissionAndStatusForUpdate.setInt(1, note.getId());
                            selectUsersFromNoteWithPermissionAndStatusForUpdate.setInt(2, CONFIRMED_STATUS);
                            selectUsersFromNoteWithPermissionAndStatusForUpdate.setInt(3, permission);
                            queryResultSet = selectUsersFromNoteWithPermissionAndStatusForUpdate.executeQuery();
                        } else {
                            throw new DataLayerException("An Unexpected Error Occurred! :(");
                            /* throw new DataLayerException("Wrong Access Mode : " + access_mode + " - SimplestNoteDataLayerMysqlImpl.getUsersFromNote(Note note, int permission, int status, int access_mode)"); - DEBUG ONLY */
                        }
                    } catch (MySQLTimeoutException ex) {
                        access_mode = READ_ACCESS;
                        connection.setAutoCommit(true);
                        selectUsersFromNoteWithPermissionAndStatus.setInt(1, note.getId());
                        selectUsersFromNoteWithPermissionAndStatus.setInt(2, status);
                        selectUsersFromNoteWithPermissionAndStatus.setInt(3, permission);
                        queryResultSet = selectUsersFromNoteWithPermissionAndStatus.executeQuery();
                    }
                } else {
                    throw new DataLayerException("An Unexpected Error Occurred! :(");
                    /* throw new DataLayerException("Wrong Status : " + status + " - SimplestNoteDataLayerMysqlImpl.getUsersFromNote(Note note, int permission, int status, int access_mode)"); - DEBUG ONLY */
                }
            } else if (permission == READONLY_PERMISSION) {
                if (status == PENDING_STATUS) {
                    try {
                        if (access_mode == READ_ACCESS) {
                            selectUsersFromNoteWithPermissionAndStatus.setInt(1, note.getId());
                            selectUsersFromNoteWithPermissionAndStatus.setInt(2, status);
                            selectUsersFromNoteWithPermissionAndStatus.setInt(3, permission);
                            queryResultSet = selectUsersFromNoteWithPermissionAndStatus.executeQuery();
                        } else if (access_mode == WRITE_ACCESS) {
                            connection.setAutoCommit(false);
                            selectUsersFromNoteWithPermissionAndStatusForUpdate.setInt(1, note.getId());
                            selectUsersFromNoteWithPermissionAndStatusForUpdate.setInt(2, status);
                            selectUsersFromNoteWithPermissionAndStatusForUpdate.setInt(3, permission);
                            queryResultSet = selectUsersFromNoteWithPermissionAndStatusForUpdate.executeQuery();
                        } else {
                            throw new DataLayerException("An Unexpected Error Occurred! :(");
                            /* throw new DataLayerException("Wrong Access Mode : " + access_mode + " - SimplestNoteDataLayerMysqlImpl.getUsersFromNote(Note note, int permission, int status, int access_mode)"); - DEBUG ONLY */
                        }
                    } catch (MySQLTimeoutException ex) {
                        access_mode = READ_ACCESS;
                        connection.setAutoCommit(true);
                        selectUsersFromNoteWithPermissionAndStatus.setInt(1, note.getId());
                        selectUsersFromNoteWithPermissionAndStatus.setInt(2, status);
                        selectUsersFromNoteWithPermissionAndStatus.setInt(3, permission);
                        queryResultSet = selectUsersFromNoteWithPermissionAndStatus.executeQuery();
                    }
                } else if (status == ALL_STATUS) {
                    try {
                        if (access_mode == READ_ACCESS) {
                            selectUsersFromNoteWithPermission.setInt(1, note.getId());
                            selectUsersFromNoteWithPermission.setInt(2, permission);
                            queryResultSet = selectUsersFromNoteWithPermission.executeQuery();
                        } else if (access_mode == WRITE_ACCESS) {
                            connection.setAutoCommit(false);
                            selectUsersFromNoteWithPermissionForUpdate.setInt(1, note.getId());
                            selectUsersFromNoteWithPermissionForUpdate.setInt(2, permission);
                            queryResultSet = selectUsersFromNoteWithPermissionForUpdate.executeQuery();
                        } else {
                            throw new DataLayerException("An Unexpected Error Occurred! :(");
                            /* throw new DataLayerException("Wrong Access Mode : " + access_mode + " - SimplestNoteDataLayerMysqlImpl.getUsersFromNote(Note note, int permission, int status, int access_mode)"); - DEBUG ONLY */
                        }
                    } catch (MySQLTimeoutException ex) {
                        access_mode = READ_ACCESS;
                        connection.setAutoCommit(true);
                        selectUsersFromNoteWithPermission.setInt(1, note.getId());
                        selectUsersFromNoteWithPermission.setInt(2, permission);
                    }
                } else if (status == CONFIRMED_STATUS) {
                    try {
                        if (access_mode == READ_ACCESS) {
                            selectUsersFromNoteWithPermissionAndStatus.setInt(1, note.getId());
                            selectUsersFromNoteWithPermissionAndStatus.setInt(2, status);
                            selectUsersFromNoteWithPermissionAndStatus.setInt(3, permission);
                            queryResultSet = selectUsersFromNoteWithPermissionAndStatus.executeQuery();
                        } else if (access_mode == WRITE_ACCESS) {
                            connection.setAutoCommit(false);
                            selectUsersFromNoteWithPermissionAndStatusForUpdate.setInt(1, note.getId());
                            selectUsersFromNoteWithPermissionAndStatusForUpdate.setInt(2, status);
                            selectUsersFromNoteWithPermissionAndStatusForUpdate.setInt(3, permission);
                            queryResultSet = selectUsersFromNoteWithPermissionAndStatusForUpdate.executeQuery();
                        } else {
                            throw new DataLayerException("An Unexpected Error Occurred! :(");
                            /* throw new DataLayerException("Wrong Access Mode : " + access_mode + " - SimplestNoteDataLayerMysqlImpl.getUsersFromNote(Note note, int permission, int status, int access_mode)"); - DEBUG ONLY */
                        }
                    } catch (MySQLTimeoutException ex) {
                        access_mode = READ_ACCESS;
                        connection.setAutoCommit(true);
                        selectUsersFromNoteWithPermissionAndStatus.setInt(1, note.getId());
                        selectUsersFromNoteWithPermissionAndStatus.setInt(2, status);
                        selectUsersFromNoteWithPermissionAndStatus.setInt(3, permission);
                        queryResultSet = selectUsersFromNoteWithPermissionAndStatus.executeQuery();
                    }
                } else {
                    throw new DataLayerException("An Unexpected Error Occurred! :(");
                    /* throw new DataLayerException("Wrong Status : " + status + " - SimplestNoteDataLayerMysqlImpl.getUsersFromNote(Note note, int permission, int status, int access_mode)"); - DEBUG ONLY */
                }
            } else if (permission == READWRITE_PERMISSION) {
                if (status == PENDING_STATUS) {
                    try {
                        if (access_mode == READ_ACCESS) {
                            selectUsersFromNoteWithPermissionAndStatus.setInt(1, note.getId());
                            selectUsersFromNoteWithPermissionAndStatus.setInt(2, status);
                            selectUsersFromNoteWithPermissionAndStatus.setInt(3, permission);
                            queryResultSet = selectUsersFromNoteWithPermissionAndStatus.executeQuery();
                        } else if (access_mode == WRITE_ACCESS) {
                            connection.setAutoCommit(false);
                            selectUsersFromNoteWithPermissionAndStatusForUpdate.setInt(1, note.getId());
                            selectUsersFromNoteWithPermissionAndStatusForUpdate.setInt(2, status);
                            selectUsersFromNoteWithPermissionAndStatusForUpdate.setInt(3, permission);
                            queryResultSet = selectUsersFromNoteWithPermissionAndStatusForUpdate.executeQuery();
                        } else {
                            throw new DataLayerException("An Unexpected Error Occurred! :(");
                            /* throw new DataLayerException("Wrong Access Mode : " + access_mode + " - SimplestNoteDataLayerMysqlImpl.getUsersFromNote(Note note, int permission, int status, int access_mode)"); - DEBUG ONLY */
                        }
                    } catch (MySQLTimeoutException ex) {
                        access_mode = READ_ACCESS;
                        connection.setAutoCommit(true);
                        selectUsersFromNoteWithPermissionAndStatus.setInt(1, note.getId());
                        selectUsersFromNoteWithPermissionAndStatus.setInt(2, status);
                        selectUsersFromNoteWithPermissionAndStatus.setInt(3, permission);
                        queryResultSet = selectUsersFromNoteWithPermissionAndStatus.executeQuery();
                    }
                } else if (status == ALL_STATUS) {
                    try {
                        if (access_mode == READ_ACCESS) {
                            selectUsersFromNoteWithPermission.setInt(1, note.getId());
                            selectUsersFromNoteWithPermission.setInt(2, permission);
                            queryResultSet = selectUsersFromNoteWithPermission.executeQuery();
                        } else if (access_mode == WRITE_ACCESS) {
                            connection.setAutoCommit(false);
                            selectUsersFromNoteWithPermissionForUpdate.setInt(1, note.getId());
                            selectUsersFromNoteWithPermissionForUpdate.setInt(2, permission);
                            queryResultSet = selectUsersFromNoteWithPermissionForUpdate.executeQuery();
                        } else {
                            throw new DataLayerException("An Unexpected Error Occurred! :(");
                            /* throw new DataLayerException("Wrong Access Mode : " + access_mode + " - SimplestNoteDataLayerMysqlImpl.getUsersFromNote(Note note, int permission, int status, int access_mode)"); - DEBUG ONLY */
                        }
                    } catch (MySQLTimeoutException ex) {
                        access_mode = READ_ACCESS;
                        connection.setAutoCommit(true);
                        selectUsersFromNoteWithPermission.setInt(1, note.getId());
                        selectUsersFromNoteWithPermission.setInt(2, permission);
                        queryResultSet = selectUsersFromNoteWithPermission.executeQuery();
                    }
                } else if (status == CONFIRMED_STATUS) {
                    try {
                        if (access_mode == READ_ACCESS) {
                            selectUsersFromNoteWithPermissionAndStatus.setInt(1, note.getId());
                            selectUsersFromNoteWithPermissionAndStatus.setInt(2, status);
                            selectUsersFromNoteWithPermissionAndStatus.setInt(3, permission);
                            queryResultSet = selectUsersFromNoteWithPermissionAndStatus.executeQuery();
                        } else if (access_mode == WRITE_ACCESS) {
                            connection.setAutoCommit(false);
                            selectUsersFromNoteWithPermissionAndStatusForUpdate.setInt(1, note.getId());
                            selectUsersFromNoteWithPermissionAndStatusForUpdate.setInt(2, status);
                            selectUsersFromNoteWithPermissionAndStatusForUpdate.setInt(3, permission);
                            queryResultSet = selectUsersFromNoteWithPermissionAndStatusForUpdate.executeQuery();
                        } else {
                            throw new DataLayerException("An Unexpected Error Occurred! :(");
                            /* throw new DataLayerException("Wrong Access Mode : " + access_mode + " - SimplestNoteDataLayerMysqlImpl.getUsersFromNote(Note note, int permission, int status, int access_mode)"); - DEBUG ONLY */
                        }
                    } catch (MySQLTimeoutException ex) {
                        access_mode = READ_ACCESS;
                        connection.setAutoCommit(true);
                        selectUsersFromNoteWithPermissionAndStatus.setInt(1, note.getId());
                        selectUsersFromNoteWithPermissionAndStatus.setInt(2, status);
                        selectUsersFromNoteWithPermissionAndStatus.setInt(3, permission);
                        queryResultSet = selectUsersFromNoteWithPermissionAndStatus.executeQuery();
                    }
                } else {
                    throw new DataLayerException("An Unexpected Error Occurred! :(");
                    /* throw new DataLayerException("Wrong Status : " + status + " - SimplestNoteDataLayerMysqlImpl.getUsersFromNote(Note note, int permission, int status, int access_mode)"); - DEBUG ONLY */
                }
            } else if (permission == ALL_PERMISSIONS) {
                if (status == PENDING_STATUS) {
                    try {
                        if (access_mode == READ_ACCESS) {
                            selectUsersFromNoteWithStatus.setInt(1, note.getId());
                            selectUsersFromNoteWithStatus.setInt(2, status);
                            queryResultSet = selectUsersFromNoteWithStatus.executeQuery();
                        } else if (access_mode == WRITE_ACCESS) {
                            connection.setAutoCommit(false);
                            selectUsersFromNoteWithStatusForUpdate.setInt(1, note.getId());
                            selectUsersFromNoteWithStatusForUpdate.setInt(2, status);
                            queryResultSet = selectUsersFromNoteWithStatusForUpdate.executeQuery();
                        } else {
                            throw new DataLayerException("An Unexpected Error Occurred! :(");
                            /* throw new DataLayerException("Wrong Access Mode : " + access_mode + " - SimplestNoteDataLayerMysqlImpl.getUsersFromNote(Note note, int permission, int status, int access_mode)"); - DEBUG ONLY */
                        }
                    } catch (MySQLTimeoutException ex) {
                        access_mode = READ_ACCESS;
                        connection.setAutoCommit(true);
                        selectUsersFromNoteWithStatus.setInt(1, note.getId());
                        selectUsersFromNoteWithStatus.setInt(2, status);
                        queryResultSet = selectUsersFromNoteWithStatus.executeQuery();
                    }
                } else if (status == ALL_STATUS) {
                    try {
                        if (access_mode == READ_ACCESS) {
                            selectUsersFromNote.setInt(1, note.getId());
                            queryResultSet = selectUsersFromNote.executeQuery();
                        } else if (access_mode == WRITE_ACCESS) {
                            connection.setAutoCommit(false);
                            selectUsersFromNoteForUpdate.setInt(1, note.getId());
                            queryResultSet = selectUsersFromNoteForUpdate.executeQuery();
                        } else {
                            throw new DataLayerException("An Unexpected Error Occurred! :(");
                            /* throw new DataLayerException("Wrong Access Mode : " + access_mode + " - SimplestNoteDataLayerMysqlImpl.getUsersFromNote(Note note, int permission, int status, int access_mode)"); - DEBUG ONLY */
                        }
                    } catch (MySQLTimeoutException ex) {
                        access_mode = READ_ACCESS;
                        connection.setAutoCommit(true);
                        selectUsersFromNote.setInt(1, note.getId());
                        queryResultSet = selectUsersFromNote.executeQuery();
                    }
                } else if (status == CONFIRMED_STATUS) {
                    try {
                        if (access_mode == READ_ACCESS) {
                            selectUsersFromNoteWithStatus.setInt(1, note.getId());
                            selectUsersFromNoteWithStatus.setInt(2, status);
                            queryResultSet = selectUsersFromNoteWithStatus.executeQuery();
                        } else if (access_mode == WRITE_ACCESS) {
                            connection.setAutoCommit(false);
                            selectUsersFromNoteWithStatusForUpdate.setInt(1, note.getId());
                            selectUsersFromNoteWithStatusForUpdate.setInt(2, status);
                            queryResultSet = selectUsersFromNoteWithStatusForUpdate.executeQuery();
                        } else {
                            throw new DataLayerException("An Unexpected Error Occurred! :(");
                            /* throw new DataLayerException("Wrong Access Mode : " + access_mode + " - SimplestNoteDataLayerMysqlImpl.getUsersFromNote(Note note, int permission, int status, int access_mode)"); - DEBUG ONLY */
                        }
                    } catch (MySQLTimeoutException ex) {
                        access_mode = READ_ACCESS;
                        connection.setAutoCommit(true);
                        selectUsersFromNoteWithStatus.setInt(1, note.getId());
                        selectUsersFromNoteWithStatus.setInt(2, status);
                        queryResultSet = selectUsersFromNoteWithStatus.executeQuery();
                    }
                } else {
                    throw new DataLayerException("An Unexpected Error Occurred! :(");
                    /* throw new DataLayerException("Wrong Status : " + status + " - SimplestNoteDataLayerMysqlImpl.getUsersFromNote(Note note, int permission, int status, int access_mode)"); - DEBUG ONLY */
                }
            } else {
                throw new DataLayerException("An Unexpected Error Occurred! :(");
                /* throw new DataLayerException("Wrong Permission : " + permission + " - SimplestNoteDataLayerMysqlImpl.getUsersFromNote(Note note, int permission, int status, int access_mode)"); - DEBUG ONLY */
            }
            if(queryResultSet != null){
                while (queryResultSet.next()) {
                result.add(getUserByKey(queryResultSet.getInt("id"), access_mode));
                }
            }
        } catch (SQLException e) {
            throw new DataLayerException("An Unexpected Error Occurred! :(");
            /* throw new DataLayerException("SQLException - SimplestNoteDataLayerMysqlImpl.getUsersFromNote(Note note, int permission, int status, int access_mode)", e); - DEBUG ONLY */
        } finally {
            try {
                if (queryResultSet != null) {
                    queryResultSet.close();
                }
            } catch (SQLException e) {/* Nothing Done*/}
        }
        return result;
    }

    @Override
    public List<Note> getNotesFromTitle(String title, int access_mode) throws DataLayerException {
        List<Note> result = new ArrayList();
        ResultSet queryResultSet = null;
        try {
            try {
                if(null != title && !title.isEmpty()){
                    if (access_mode == READ_ACCESS) {
                        selectNotesByTitle.setString(1, "%" + title + "%");
                        queryResultSet = selectNotesByTitle.executeQuery();
                    } else if (access_mode == WRITE_ACCESS) {
                        connection.setAutoCommit(false);
                        selectNotesByTitleForUpdate.setString(1, "%" + title + "%");
                        queryResultSet = selectNotesByTitleForUpdate.executeQuery();
                    } else {
                        throw new DataLayerException("An Unexpected Error Occurred! :(");
                        /* throw new DataLayerException("Wrong Access Mode - SimplestNoteDataLayerMysqlImpl.getNotesFromTitle(String title, int access_mode)"); */
                    }
                } else {
                    throw new DataLayerException("An Unexpected Error Occurred! :(");
                    /* throw new DataLayerException("Title String Error : " + ((null != title)? ((title.isEmpty())? "Empty Title String" : title) : "Null Title String") + " - SimplestNoteDataLayerMysqlImpl.getNotesFromTitle(String title, int access_mode)"); - DEBUG ONLY */
                }
            } catch (MySQLTimeoutException ex) {
                access_mode = READ_ACCESS;
                connection.setAutoCommit(true);
                selectNotesByTitle.setString(1, "%" + title + "%");
                queryResultSet = selectNotesByTitle.executeQuery();
            }
            while (queryResultSet.next()) {
                result.add(getNoteByKey(queryResultSet.getInt("id"), queryResultSet.getInt("version"), access_mode));
            }
        } catch (SQLException e) {
            throw new DataLayerException("An Unexpected Error Occurred! :(");
            /* throw new DataLayerException("SQLException - SimplestNoteDataLayerMysqlImpl.getNotesFromTitle(String title, int access_mode)", e); */
        } finally {
            try {
                if (queryResultSet != null) {
                    queryResultSet.close();
                }
            } catch (SQLException e) {/*Nothing Done*/}
        }
        return result;
    }

    @Override
    public List<Note> getNotesFromUser(User user, int permission, int status, int access_mode) throws DataLayerException {
        List<Note> result = new ArrayList();
        ResultSet queryResultSet = null;
        try {
            if (null == user) {
                throw new DataLayerException("An Unexpected Error Occurred! :(");
                /* throw new DataLayerException("Null User Instance - SimplestNoteDataLayerMysqlImpl.getNotesFromUser(User user, int permission, int status, int access_mode)"); - DEBUG ONLY */
            }
            if (permission == CREATOR_PERMISSION) {
                if (status == CONFIRMED_STATUS || status == ALL_STATUS) {
                    try {
                        if (access_mode == READ_ACCESS) {
                            selectNotesFromUserWithPermissionAndStatus.setInt(1, user.getId());
                            selectNotesFromUserWithPermissionAndStatus.setInt(2, permission);
                            selectNotesFromUserWithPermissionAndStatus.setInt(3, status);
                            queryResultSet = selectNotesFromUserWithPermissionAndStatus.executeQuery();
                        } else if (access_mode == WRITE_ACCESS) {
                            connection.setAutoCommit(false);
                            selectNotesFromUserWithPermissionAndStatusForUpdate.setInt(1, user.getId());
                            selectNotesFromUserWithPermissionAndStatusForUpdate.setInt(2, permission);
                            selectNotesFromUserWithPermissionAndStatusForUpdate.setInt(3, status);
                            queryResultSet = selectNotesFromUserWithPermissionAndStatusForUpdate.executeQuery();
                        } else {
                            throw new DataLayerException("An Unexpected Error Occurred! :(");
                            /* throw new DataLayerException("Wrong Access Mode : " + access_mode + " - SimplestNoteDataLayerMysqlImpl.getNotesFromUser(User user, int permission, int status, int access_mode)"); - DEBUG ONLY */
                        }
                    } catch (MySQLTimeoutException ex) {
                        access_mode = READ_ACCESS;
                        connection.setAutoCommit(true);
                        selectNotesFromUserWithPermissionAndStatus.setInt(1, user.getId());
                        selectNotesFromUserWithPermissionAndStatus.setInt(2, CREATOR_PERMISSION);
                        selectNotesFromUserWithPermissionAndStatus.setInt(3, CONFIRMED_STATUS);
                        queryResultSet = selectNotesFromUserWithPermissionAndStatus.executeQuery();
                    }
                } else {
                    throw new DataLayerException("An Unexpected Error Occurred! :(");
                    /* throw new DataLayerException("Wrong Status : " + status + " - SimplestNoteDataLayerMysqlImpl.getNotesFromUser(User user, int permission, int status, int access_mode)"); - DEBUG ONLY */
                }
            } else if (permission == READONLY_PERMISSION) {
                if (status == CONFIRMED_STATUS) {
                    try {
                        if (access_mode == READ_ACCESS) {
                            selectNotesFromUserWithPermissionAndStatus.setInt(1, user.getId());
                            selectNotesFromUserWithPermissionAndStatus.setInt(2, READONLY_PERMISSION);
                            selectNotesFromUserWithPermissionAndStatus.setInt(3, CONFIRMED_STATUS);
                            queryResultSet = selectNotesFromUserWithPermissionAndStatus.executeQuery();
                        } else if (access_mode == WRITE_ACCESS) {
                            connection.setAutoCommit(false);
                            selectNotesFromUserWithPermissionAndStatusForUpdate.setInt(1, user.getId());
                            selectNotesFromUserWithPermissionAndStatusForUpdate.setInt(2, READONLY_PERMISSION);
                            selectNotesFromUserWithPermissionAndStatusForUpdate.setInt(3, CONFIRMED_STATUS);
                            queryResultSet = selectNotesFromUserWithPermissionAndStatusForUpdate.executeQuery();
                        } else {
                            throw new DataLayerException("An Unexpected Error Occurred! :(");
                            /* throw new DataLayerException("Wrong Access Mode : " + access_mode + " - SimplestNoteDataLayerMysqlImpl.getNotesFromUser(User user, int permission, int status, int access_mode)"); - DEBUG ONLY */
                        }
                    } catch (MySQLTimeoutException ex) {
                        access_mode = READ_ACCESS;
                        connection.setAutoCommit(true);
                        selectNotesFromUserWithPermissionAndStatus.setInt(1, user.getId());
                        selectNotesFromUserWithPermissionAndStatus.setInt(2, READONLY_PERMISSION);
                        selectNotesFromUserWithPermissionAndStatus.setInt(3, CONFIRMED_STATUS);
                        queryResultSet = selectNotesFromUserWithPermissionAndStatus.executeQuery();
                    }
                } else if (status == PENDING_STATUS) {
                    try {
                        if (access_mode == READ_ACCESS) {
                            selectNotesFromUserWithPermissionAndStatus.setInt(1, user.getId());
                            selectNotesFromUserWithPermissionAndStatus.setInt(2, READONLY_PERMISSION);
                            selectNotesFromUserWithPermissionAndStatus.setInt(3, PENDING_STATUS);
                            queryResultSet = selectNotesFromUserWithPermissionAndStatus.executeQuery();
                        } else if (access_mode == WRITE_ACCESS) {
                            connection.setAutoCommit(false);
                            selectNotesFromUserWithPermissionAndStatusForUpdate.setInt(1, user.getId());
                            selectNotesFromUserWithPermissionAndStatusForUpdate.setInt(2, READONLY_PERMISSION);
                            selectNotesFromUserWithPermissionAndStatusForUpdate.setInt(3, PENDING_STATUS);
                            queryResultSet = selectNotesFromUserWithPermissionAndStatusForUpdate.executeQuery();
                        } else {
                            throw new DataLayerException("An Unexpected Error Occurred! :(");
                            /* throw new DataLayerException("Wrong Access Mode : " + access_mode + " - SimplestNoteDataLayerMysqlImpl.getNotesFromUser(User user, int permission, int status, int access_mode)"); - DEBUG ONLY */
                        }
                    } catch (MySQLTimeoutException ex) {
                        access_mode = READ_ACCESS;
                        connection.setAutoCommit(true);
                        selectNotesFromUserWithPermissionAndStatus.setInt(1, user.getId());
                        selectNotesFromUserWithPermissionAndStatus.setInt(2, READONLY_PERMISSION);
                        selectNotesFromUserWithPermissionAndStatus.setInt(3, PENDING_STATUS);
                        queryResultSet = selectNotesFromUserWithPermissionAndStatus.executeQuery();
                    }
                } else if (status == ALL_STATUS) {
                    try {
                        if (access_mode == READ_ACCESS) {
                            selectNotesFromUserWithPermission.setInt(1, user.getId());
                            selectNotesFromUserWithPermission.setInt(2, READONLY_PERMISSION);
                            queryResultSet = selectNotesFromUserWithPermission.executeQuery();
                        } else if (access_mode == WRITE_ACCESS) {
                            connection.setAutoCommit(false);
                            selectNotesFromUserWithPermissionForUpdate.setInt(1, user.getId());
                            selectNotesFromUserWithPermissionForUpdate.setInt(2, READONLY_PERMISSION);
                            queryResultSet = selectNotesFromUserWithPermissionForUpdate.executeQuery();
                        } else {
                            throw new DataLayerException("An Unexpected Error Occurred! :(");
                            /* throw new DataLayerException("Wrong Access Mode : " + access_mode + " - SimplestNoteDataLayerMysqlImpl.getNotesFromUser(User user, int permission, int status, int access_mode)"); - DEBUG ONLY */
                        }
                    } catch (MySQLTimeoutException ex) {
                        access_mode = READ_ACCESS;
                        connection.setAutoCommit(true);
                        selectNotesFromUserWithPermission.setInt(1, user.getId());
                        selectNotesFromUserWithPermission.setInt(2, READONLY_PERMISSION);
                        queryResultSet = selectNotesFromUserWithPermission.executeQuery();
                    }
                } else {
                    throw new DataLayerException("An Unexpected Error Occurred! :(");
                            /* throw new DataLayerException("Wrong Status : " + status + " - SimplestNoteDataLayerMysqlImpl.getNotesFromUser(User user, int permission, int status, int access_mode)"); - DEBUG ONLY */
                }
            } else if (permission == READWRITE_PERMISSION) {
                if (status == CONFIRMED_STATUS) {
                    try {
                        if (access_mode == READ_ACCESS) {
                            selectNotesFromUserWithPermissionAndStatus.setInt(1, user.getId());
                            selectNotesFromUserWithPermissionAndStatus.setInt(2, READWRITE_PERMISSION);
                            selectNotesFromUserWithPermissionAndStatus.setInt(3, CONFIRMED_STATUS);
                            queryResultSet = selectNotesFromUserWithPermissionAndStatus.executeQuery();
                        } else if (access_mode == WRITE_ACCESS) {
                            connection.setAutoCommit(false);
                            selectNotesFromUserWithPermissionAndStatusForUpdate.setInt(1, user.getId());
                            selectNotesFromUserWithPermissionAndStatusForUpdate.setInt(2, READWRITE_PERMISSION);
                            selectNotesFromUserWithPermissionAndStatusForUpdate.setInt(3, CONFIRMED_STATUS);
                            queryResultSet = selectNotesFromUserWithPermissionAndStatusForUpdate.executeQuery();
                        } else {
                            throw new DataLayerException("An Unexpected Error Occurred! :(");
                            /* throw new DataLayerException("Wrong Access Mode : " + access_mode + " - SimplestNoteDataLayerMysqlImpl.getNotesFromUser(User user, int permission, int status, int access_mode)"); - DEBUG ONLY */
                        }
                    } catch (MySQLTimeoutException ex) {
                        access_mode = READ_ACCESS;
                        connection.setAutoCommit(true);
                        selectNotesFromUserWithPermissionAndStatus.setInt(1, user.getId());
                        selectNotesFromUserWithPermissionAndStatus.setInt(2, READWRITE_PERMISSION);
                        selectNotesFromUserWithPermissionAndStatus.setInt(3, CONFIRMED_STATUS);
                        queryResultSet = selectNotesFromUserWithPermissionAndStatus.executeQuery();
                    }
                } else if (status == PENDING_STATUS) {
                    try {
                        if (access_mode == READ_ACCESS) {
                            selectNotesFromUserWithPermissionAndStatus.setInt(1, user.getId());
                            selectNotesFromUserWithPermissionAndStatus.setInt(2, READWRITE_PERMISSION);
                            selectNotesFromUserWithPermissionAndStatus.setInt(3, PENDING_STATUS);
                            queryResultSet = selectNotesFromUserWithPermissionAndStatus.executeQuery();
                        } else if (access_mode == WRITE_ACCESS) {
                            connection.setAutoCommit(false);
                            selectNotesFromUserWithPermissionAndStatusForUpdate.setInt(1, user.getId());
                            selectNotesFromUserWithPermissionAndStatusForUpdate.setInt(2, READWRITE_PERMISSION);
                            selectNotesFromUserWithPermissionAndStatusForUpdate.setInt(3, PENDING_STATUS);
                            queryResultSet = selectNotesFromUserWithPermissionAndStatusForUpdate.executeQuery();
                        } else {
                            throw new DataLayerException("An Unexpected Error Occurred! :(");
                            /* throw new DataLayerException("Wrong Access Mode : " + access_mode + " - SimplestNoteDataLayerMysqlImpl.getNotesFromUser(User user, int permission, int status, int access_mode)"); - DEBUG ONLY */
                        }
                    } catch (MySQLTimeoutException ex) {
                        access_mode = READ_ACCESS;
                        connection.setAutoCommit(true);
                        selectNotesFromUserWithPermissionAndStatus.setInt(1, user.getId());
                        selectNotesFromUserWithPermissionAndStatus.setInt(2, READWRITE_PERMISSION);
                        selectNotesFromUserWithPermissionAndStatus.setInt(3, PENDING_STATUS);
                        queryResultSet = selectNotesFromUserWithPermissionAndStatus.executeQuery();
                    }
                } else if (status == ALL_STATUS) {
                    try {
                        if (access_mode == READ_ACCESS) {
                            selectNotesFromUserWithPermission.setInt(1, user.getId());
                            selectNotesFromUserWithPermission.setInt(2, READWRITE_PERMISSION);
                            queryResultSet = selectNotesFromUserWithPermission.executeQuery();
                        } else if (access_mode == WRITE_ACCESS) {
                            selectNotesFromUserWithPermissionForUpdate.setInt(1, user.getId());
                            selectNotesFromUserWithPermissionForUpdate.setInt(2, READWRITE_PERMISSION);
                            queryResultSet = selectNotesFromUserWithPermissionForUpdate.executeQuery();
                        } else {
                            throw new DataLayerException("An Unexpected Error Occurred! :(");
                            /* throw new DataLayerException("Wrong Access Mode : " + access_mode + " - SimplestNoteDataLayerMysqlImpl.getNotesFromUser(User user, int permission, int status, int access_mode)"); - DEBUG ONLY */
                        }
                    } catch (MySQLTimeoutException ex) {
                        access_mode = READ_ACCESS;
                        connection.setAutoCommit(true);
                        selectNotesFromUserWithPermission.setInt(1, user.getId());
                        selectNotesFromUserWithPermission.setInt(2, READWRITE_PERMISSION);
                        queryResultSet = selectNotesFromUserWithPermission.executeQuery();
                    }
                } else {
                    throw new DataLayerException("An Unexpected Error Occurred! :(");
                    /* throw new DataLayerException("Wrong Status : " + status + " - SimplestNoteDataLayerMysqlImpl.getNotesFromUser(User user, int permission, int status, int access_mode)"); - DEBUG ONLY */
                }
            } else if (permission == ALL_PERMISSIONS) {
                if (status == CONFIRMED_STATUS) {
                    try {
                        if (access_mode == READ_ACCESS) {
                            selectNotesFromUserWithStatus.setInt(1, user.getId());
                            selectNotesFromUserWithStatus.setInt(2, CONFIRMED_STATUS);
                            queryResultSet = selectNotesFromUserWithStatus.executeQuery();
                        } else if (access_mode == WRITE_ACCESS) {
                            selectNotesFromUserWithStatusForUpdate.setInt(1, user.getId());
                            selectNotesFromUserWithStatusForUpdate.setInt(2, CONFIRMED_STATUS);
                            queryResultSet = selectNotesFromUserWithStatusForUpdate.executeQuery();
                        } else {
                            throw new DataLayerException("An Unexpected Error Occurred! :(");
                            /* throw new DataLayerException("Wrong Access Mode : " + access_mode + " - SimplestNoteDataLayerMysqlImpl.getNotesFromUser(User user, int permission, int status, int access_mode)"); - DEBUG ONLY */
                        }
                    } catch (MySQLTimeoutException ex) {
                        access_mode = READ_ACCESS;
                        connection.setAutoCommit(true);
                        selectNotesFromUserWithStatus.setInt(1, user.getId());
                        selectNotesFromUserWithStatus.setInt(2, CONFIRMED_STATUS);
                        queryResultSet = selectNotesFromUserWithStatus.executeQuery();
                    }
                } else if (status == PENDING_STATUS) {
                    try {
                        if (access_mode == READ_ACCESS) {
                            selectNotesFromUserWithStatus.setInt(1, user.getId());
                            selectNotesFromUserWithStatus.setInt(2, PENDING_STATUS);
                            queryResultSet = selectNotesFromUserWithStatus.executeQuery();
                        } else if (access_mode == WRITE_ACCESS) {
                            connection.setAutoCommit(false);
                            selectNotesFromUserWithStatusForUpdate.setInt(1, user.getId());
                            selectNotesFromUserWithStatusForUpdate.setInt(2, PENDING_STATUS);
                            queryResultSet = selectNotesFromUserWithStatusForUpdate.executeQuery();
                        } else {
                            throw new DataLayerException("An Unexpected Error Occurred! :(");
                            /* throw new DataLayerException("Wrong Access Mode : " + access_mode + " - SimplestNoteDataLayerMysqlImpl.getNotesFromUser(User user, int permission, int status, int access_mode)"); - DEBUG ONLY */
                        }
                    } catch (MySQLTimeoutException ex) {
                        access_mode = READ_ACCESS;
                        connection.setAutoCommit(true);
                        selectNotesFromUserWithStatus.setInt(1, user.getId());
                        selectNotesFromUserWithStatus.setInt(2, PENDING_STATUS);
                        queryResultSet = selectNotesFromUserWithStatus.executeQuery();
                    }
                } else if (status == ALL_STATUS) {
                    try {
                        if (access_mode == READ_ACCESS) {
                            selectNotesFromUser.setInt(1, user.getId());
                            queryResultSet = selectNotesFromUser.executeQuery();
                        } else if (access_mode == WRITE_ACCESS) {
                            connection.setAutoCommit(false);
                            selectNotesFromUserForUpdate.setInt(1, user.getId());
                            queryResultSet = selectNotesFromUser.executeQuery();
                        } else {
                            throw new DataLayerException("An Unexpected Error Occurred! :(");
                            /* throw new DataLayerException("Wrong Access Mode : " + access_mode + " - SimplestNoteDataLayerMysqlImpl.getNotesFromUser(User user, int permission, int status, int access_mode)"); - DEBUG ONLY */
                        }
                    } catch (MySQLTimeoutException ex) {
                        access_mode = READ_ACCESS;
                        connection.setAutoCommit(true);
                        selectNotesFromUser.setInt(1, user.getId());
                        queryResultSet = selectNotesFromUser.executeQuery();
                    }
                } else {
                    throw new DataLayerException("An Unexpected Error Occurred! :(");
                    /* throw new DataLayerException("Wrong Status : " + status + " - SimplestNoteDataLayerMysqlImpl.getNotesFromUser(User user, int permission, int status, int access_mode)"); - DEBUG ONLY */
                }
            } else {
                throw new DataLayerException("An Unexpected Error Occurred! :(");
                /* throw new DataLayerException("Wrong permission : " + permission + " - SimplestNoteDataLayerMysqlImpl.getNotesFromUser(Note note, int permission, int status, int access_mode)"); */
            }
            while (queryResultSet.next()) {
                result.add(getNoteByKey(queryResultSet.getInt("id"), queryResultSet.getInt("version"), access_mode));
            }
        } catch (SQLException e) {
            throw new DataLayerException("An Unexpected Error Occurred! :(");
            /* throw new DataLayerException("SQLException - SimplestNoteDataLayerMysqlImpl.getNotesFromUser(User user, int permission, int status, int access_mode)", e); - DEBUG ONLY */
        } finally {
            try {
                if (queryResultSet != null) {
                    queryResultSet.close();
                }
            } catch (SQLException e) {/*Nothing Done*/}
        }
        return result;
    }

    @Override
    public List<Note> getNotesFromTag(Tag tag, int access_mode) throws DataLayerException {
        List<Note> result = new ArrayList();
        ResultSet queryResultSet = null;
        try {
            if (null == tag) {
                throw new DataLayerException("An Unexpected Error Occurred! :(");
                /* throw new DataLayerException("Null Tag Instance Error - SimplestNoteDataLayerMysqlImpl.getNotesFromTag(Tag tag, int access_mode)"); - DEBUG ONLY */
            }
            try {
                if (access_mode == READ_ACCESS) {
                    selectNotesFromTag.setInt(1, tag.getId());
                    queryResultSet = selectNotesFromTag.executeQuery();
                } else if (access_mode == WRITE_ACCESS) {
                    connection.setAutoCommit(false);
                    selectNotesFromTagForUpdate.setInt(1, tag.getId());
                    queryResultSet = selectNotesFromTagForUpdate.executeQuery();
                } else {
                    throw new DataLayerException("An Unexpected Error Occurred! :(");
                    /* throw new DataLayerException("Wrong Access Mode - SimplestNoteDataLayerMysqlImpl.getNotesFromTag(Tag tag, int access_mode)"); - DEBUG ONLY */
                }
            } catch (MySQLTimeoutException ex) {
                access_mode = READ_ACCESS;
                connection.setAutoCommit(true);
                selectNotesFromTag.setInt(1, tag.getId());
                queryResultSet = selectNotesFromTag.executeQuery();
            }
            while (queryResultSet.next()) {
                result.add(getNoteByKey(queryResultSet.getInt("id"), queryResultSet.getInt("version"), access_mode));
            }
        } catch (SQLException e) {
            throw new DataLayerException("An Unexpected Error Occurred! :(");
            /* throw new DataLayerException("SQLException - SimplestNoteDataLayerMysqlImpl.getNotesFromTag(Tag tag, int access_mode)", e); - DEBUG ONLY */
        } finally {
            try {
                if (queryResultSet != null) {
                    queryResultSet.close();
                }
            } catch (SQLException e) {/*Nothing Done*/}
        }
        return result;
    }

    @Override
    public List<Note> getNoteHistory(Note note, int access_mode) throws DataLayerException {
        List<Note> result = new ArrayList();
        ResultSet queryResultSet = null;
        try {
            if(null == note) {
                throw new DataLayerException("An Unexpected Error Occurred! :(");
                /* throw new DataLayerException("Null Note Instance - SimplestNoteDataLayerMysqlImpl.getNoteHistory(Note note, int access_mode)"); */
            }
            try {
                if (access_mode == READ_ACCESS) {
                    selectNoteHistory.setInt(1, note.getId());
                    selectNoteHistory.setInt(2, note.getId());
                    queryResultSet = selectNoteHistory.executeQuery();
                } else if (access_mode == WRITE_ACCESS) {
                    connection.setAutoCommit(false);
                    selectNoteHistoryForUpdate.setInt(1, note.getId());
                    selectNoteHistoryForUpdate.setInt(2, note.getId());
                    queryResultSet = selectNoteHistoryForUpdate.executeQuery();
                } else {
                    throw new DataLayerException("An Unexpected Error Occurred! :(");
                    /* throw new DataLayerException("Wrong Access Mode - SimplestNoteDataLayerMysqlImpl.getNoteHistory(Note note, int access_mode)"); */
                }
            } catch (MySQLTimeoutException ex) {
                access_mode = READ_ACCESS;
                connection.setAutoCommit(true);
                selectNoteHistory.setInt(1, note.getId());
                selectNoteHistory.setInt(2, note.getId());
                queryResultSet = selectNoteHistory.executeQuery();
            }
            while (queryResultSet.next()) {
                result.add(getNoteByKey(queryResultSet.getInt("id"), queryResultSet.getInt("version"), access_mode));
            }
        } catch (SQLException e) {
            throw new DataLayerException("An Unexpected Error Occurred! :(");
            /* throw new DataLayerException("SQLException - SimplestNoteDataLayerMysqlImpl.getNoteHistory(Note note, int access_mode)", e); */
        } finally {
            try {
                if (queryResultSet != null) {
                    queryResultSet.close();
                }
            } catch (SQLException e) {/*Nothing Done*/}
        }
        return result;
    }

    @Override
    public List<Tag> getTagsFromContent(String content, int access_mode) throws DataLayerException {
        List<Tag> result = new ArrayList();
        ResultSet queryResultSet = null;
        try {
            if( null == content ){
                throw new DataLayerException("An Unexpected Error Occurred! :(");
                /* throw new DataLayerException("Null Content String Error - SimplestNoteDataLayerMysqlImpl.getTagsFromContent(String content, int access_mode)"); - DEBUG ONLY */
            }
            try {
                if (access_mode == READ_ACCESS) {
                    selectTagsByContent.setString(1, "%" + content + "%");
                    queryResultSet = selectTagsByContent.executeQuery();
                } else if (access_mode == WRITE_ACCESS) {
                    connection.setAutoCommit(false);
                    selectTagsByContentForUpdate.setString(1, "%" + content + "%");
                    queryResultSet = selectTagsByContentForUpdate.executeQuery();
                } else {
                    throw new DataLayerException("An Unexpected Error Occurred! :(");
                    /* throw new DataLayerException("Wrong Access Mode : " + access_mode + " - SimplestNoteDataLayerMysqlImpl.getTagsFromContent(String content, int access_mode)");*/
                }
            } catch (MySQLTimeoutException ex) {
                access_mode = READ_ACCESS;
                connection.setAutoCommit(true);
                selectTagsByContent.setString(1, "%" + content + "%");
                queryResultSet = selectTagsByContent.executeQuery();
            }
            while (queryResultSet.next()) {
                result.add(getTagByKey(queryResultSet.getInt("id"), access_mode));
            }
        } catch (SQLException e) {
            throw new DataLayerException("An Unexpected Error Occurred! :(");
            /* throw new DataLayerException("SQLException - SimplestNoteDataLayerMysqlImpl.getTagsFromContent(String content, int access_mode)", e); */
        } finally {
            try {
                if (queryResultSet != null) {
                    queryResultSet.close();
                }
            } catch (SQLException e) {/* Nothing Done */}
        }
        return result;
    }

    @Override
    public List<Tag> getTagsFromUser(User user, int access_mode) throws DataLayerException {
        List<Tag> result = new ArrayList();
        ResultSet queryResultSet = null;
        try {
            if (null == user) {
                throw new DataLayerException("An Unexpected Error Occurred! :(");
                /* throw new DataLayerException("Null User Instance Error - SimplestNoteDataLayerMysqlImpl.getTagsFromUser(User user, int access_mode)"); - DEBUG ONLY */
            }
            try {
                if (access_mode == READ_ACCESS) {
                    selectTagsFromUser.setInt(1, user.getId());
                    queryResultSet = selectTagsFromUser.executeQuery();
                } else if (access_mode == WRITE_ACCESS) {
                    connection.setAutoCommit(false);
                    selectTagsFromUserForUpdate.setInt(1, user.getId());
                    queryResultSet = selectTagsFromUserForUpdate.executeQuery();
                } else {
                    throw new DataLayerException("An Unexpected Error Occurred! :(");
                    /* throw new DataLayerException("Wrong Access Mode : " + access_mode + " - SimplestNoteDataLayerMysqlImpl.getTagsFromUser(User user, int access_mode)"); - DEBUG ONLY */
                }
            } catch (MySQLTimeoutException ex) {
                access_mode = READ_ACCESS;
                connection.setAutoCommit(true);
                selectTagsFromUser.setInt(1, user.getId());
                queryResultSet = selectTagsFromUser.executeQuery();
            }
            while (queryResultSet.next()) {
                result.add(getTagByKey(queryResultSet.getInt("id"), access_mode));
            }
        } catch (SQLException e) {
            throw new DataLayerException("An Unexpected Error Occurred! :(");
            /* throw new DataLayerException("SQLException - SimplestNoteDataLayerMysqlImpl.getTagsFromUser(User user, int access_mode)", e); */
        } finally {
            try {
                if (queryResultSet != null) {
                    queryResultSet.close();
                }
            } catch (SQLException e) {/*Nothing Done*/}
        }
        return result;
    }

    @Override
    public int getTagInclusionsNumber(Tag tag) throws DataLayerException {
        ResultSet queryResultSet = null;
        try {
            if(null != tag){
                selectTagInclusionsNumber.setInt(1, tag.getId());
                queryResultSet = selectTagInclusionsNumber.executeQuery();
                if (queryResultSet.next()) {
                    return queryResultSet.getInt("tag_inclusions_number");
                } else {
                    return 0;
                }
            } else {
                throw new DataLayerException("An Unexpected Error Occurred! :(");
                /* throw new DataLayerException("Null Tag Instance Error - SimplestNoteDataLayerMysqlImpl.getTagInclusionsNumber(Tag tag)"); */
            }
        } catch (SQLException ex) {
            throw new DataLayerException("An Unexpected Error Occurred");
            /* throw new DataLayerException("SQLException - SimplestNoteDataLayerMysqlImpl.getTagInclusionsNumber(Tag tag)", e); */
        } finally {
            try {
                if (queryResultSet != null) {
                    queryResultSet.close();
                }
            } catch (SQLException e) {/*Nothing Done*/}

        }
    }

    @Override
    public List<Tag> getTagsFromNote(Note note, int access_mode) throws DataLayerException {
        List<Tag> result = new ArrayList();
        ResultSet queryResultSet = null;
        try {
            if (null == note) {
                throw new DataLayerException("An Unexpected Error Occurred! :(");
                /* throw new DataLayerException("Null Note Instance Error - SimplestNoteDataLayerMysqlImpl.getTagsFromNote(Note note, int access_mode)") - DEBUG ONLY */
            }
            try {
                if (access_mode == READ_ACCESS) {
                    selectTagsFromNote.setInt(1, note.getId());
                    selectTagsFromNote.setInt(2, note.getVersion());
                    queryResultSet = selectTagsFromNote.executeQuery();
                } else if (access_mode == WRITE_ACCESS) {
                    connection.setAutoCommit(false);
                    selectTagsFromNoteForUpdate.setInt(1, note.getId());
                    selectTagsFromNoteForUpdate.setInt(2, note.getVersion());
                    queryResultSet = selectTagsFromNoteForUpdate.executeQuery();
                } else {
                    throw new DataLayerException("An Unexpected Error Occurred! :(");
                    /* throw new DataLayerException("Wrong Access Mode : " + access_mode + " - SimplestNoteDataLayerMysqlImpl.getTagsFromNote(Note note, int access_mode)"); - DEBUG ONLY */
                }
            } catch (MySQLTimeoutException ex) {
                access_mode = READ_ACCESS;
                connection.setAutoCommit(true);
                selectTagsFromNote.setInt(1, note.getId());
                selectTagsFromNote.setInt(2, note.getVersion());
                queryResultSet = selectTagsFromNote.executeQuery();
            }
            while (queryResultSet.next()) {
                result.add(getTagByKey(queryResultSet.getInt("id"), access_mode));
            }
        } catch (SQLException e) {
            throw new DataLayerException("An Unexpected Error Occurred! :(");
            /* throw new DataLayerException("SQLException - SimplestNoteDataLayerMysqlImpl.getTagsFromNote(Note note, int access_mode)", e); - DEBUG ONLY */
        } finally {
            try {
                if (queryResultSet != null) {
                    queryResultSet.close();
                }
            } catch (SQLException e) {/*Nothing Done*/}
        }
        return result;
    }

    @Override
    public void storeUser(User user) throws DataLayerException {
        
        if( null != user ){
            ResultSet keys = null;
            int key = user.getId();
            try {
                if (key > 0) {
                    if (!user.isDirty()) {
                        return;
                    }
                    updateUser.setString(1, user.getFirstName());
                    updateUser.setString(2, user.getLastName());
                    updateUser.setString(3, user.getEmailAddress());
                    updateUser.setString(4, user.getPassword());
                    updateUser.setInt(5, user.getId());
                    updateUser.executeUpdate();
                } else {
                    insertUser.setString(1, user.getFirstName());
                    insertUser.setString(2, user.getLastName());
                    insertUser.setString(3, user.getEmailAddress());
                    insertUser.setString(4, user.getPassword());
                    if (insertUser.executeUpdate() == 1) {
                        keys = insertUser.getGeneratedKeys();
                        if (keys.next()) {
                            key = keys.getInt(1);
                        }
                    }
                }
                if(key > 0){
                    user.copyFrom(getUserByKey(key, user.getAccessMode()));
                }
                user.setDirty(false);
            } catch (SQLException ex) {
                throw new DataLayerException("An Unexpected Error Occurred! :(");
                /* throw new DataLayerException("SQLException - SimplestNoteDataLayerMysqlImpl.storeUser(User user)", ex); - DEBUG ONLY */
            } finally {
                try {
                    if (keys != null) {
                        keys.close();
                    }
                    if (user.getAccessMode() == WRITE_ACCESS) {
                        connection.commit();
                        connection.setAutoCommit(true);
                    }
                } catch (SQLException ex) {/* Nothing Done */}
            }
        } else {
            throw new DataLayerException("An Unexpected Error Occurred! :(");
            /* throw new DataLayerException("Null User Instance Error - SimplestNoteDataLayerMysqlImpl.storeUser(User user)"); - DEBUG ONLY */
        }
        
        
    }
    @Override
    public void storeNote(Note note) throws DataLayerException {
        
        if(null != note){
            ResultSet keys = null;
            int key = note.getId();
            try {
                User currentCreatorUser = note.getCreatorUser(READ_ACCESS);
                if (key > 0) { 
                    if (!note.isDirty()) {
                        return;
                    }
                    updateNoteStatic.setInt(1, key);
                    updateNoteStatic.executeUpdate();
                    Note storedNote = getNoteByKey(key, note.getVersion(), READ_ACCESS);
                    if (levenshteinDistance(note.getContentFile(), storedNote.getContentFile()) >= 15) {
                        note.setVersion(note.getVersion() + 1);
                        note.setContentPath(note.getContentPath().substring(0, note.getContentPath().lastIndexOf("/")).substring(0, note.getContentPath().substring(0, note.getContentPath().lastIndexOf("/")).lastIndexOf("/")) + "/" + note.getVersion() + "/" + note.getTitle().replaceAll(" ", "") + ".txt"); 
                        File noteFile = new File(note.getContentPath());
                        File noteFileParent = noteFile.getParentFile();
                        if (!noteFileParent.exists() && !noteFileParent.mkdirs()) {
                            throw new DataLayerException("An Unexpected Error Occurred! :(");
                            /* throw new DataLayerException("File System Error ( Couldn't create directory " + noteFileParent + " ) - SimplestNoteDataLayerMysqlImpl.storeNote(Note note)"); - DEBUG ONLY */
                        }
                        FileWriter noteFileWriter = new FileWriter(noteFile);
                        try (BufferedWriter noteBufferedWriter = new BufferedWriter(noteFileWriter)) {
                            noteBufferedWriter.write(note.getContentFile());
                        }
                        insertNoteDynamic.setInt(1, key);
                        insertNoteDynamic.setInt(2, note.getVersion());
                        insertNoteDynamic.setString(3, note.getTitle());
                        insertNoteDynamic.setString(4, note.getContentPath());
                        insertNoteDynamic.executeUpdate();
                    } else {
                        File currentNoteVersionFolderFile = new File(note.getContentPath());
                        if (!delFileRecursively(currentNoteVersionFolderFile)) {
                            throw new DataLayerException("An Unexpected Error Occurred! :(");
                            /* throw new DataLayerException("File System Error ( Couldn't create directory " + currentNoteVersionFolderFile + " ) - SimplestNoteDataLayerMysqlImpl.storeNote(Note note)"); - DEBUG ONLY */
                        }
                        File noteFile = new File(note.getContentPath().substring(0, note.getContentPath().lastIndexOf("/")) + "/" + note.getTitle().replaceAll(" ", "") + ".txt");
                        note.setContentPath(note.getContentPath().substring(0, note.getContentPath().lastIndexOf("/")) + "/" + note.getTitle().replaceAll(" ", "") + ".txt");
                        File noteFileParent = noteFile.getParentFile();
                        if (!noteFileParent.exists() && !noteFileParent.mkdirs()) {
                            throw new DataLayerException("An Unexpected Error Occurred! :(");
                            /* throw new DataLayerException("File System Error ( Couldn't create directory " + noteFileParent + " ) - SimplestNoteDataLayerMysqlImpl.storeNote(Note note)"); - DEBUG ONLY */
                        }
                        FileWriter noteFileWriter = new FileWriter(noteFile);
                        try (BufferedWriter noteBufferedWriter = new BufferedWriter(noteFileWriter)) {
                            noteBufferedWriter.write(note.getContentFile());
                        }
                        updateNoteDynamic.setString(1, note.getTitle());
                        updateNoteDynamic.setString(2, note.getContentPath());
                        updateNoteDynamic.setInt(3, key);
                        updateNoteDynamic.setInt(4, note.getVersion());
                        updateNoteDynamic.executeUpdate();
                    }
                } else {
                    /* contentPath :  getServletContext().getRealPath("/") + "../../users/" && contentFile : editor */
                    if (insertNoteStatic.executeUpdate() == 1) { 
                        keys = insertNoteStatic.getGeneratedKeys();
                        if (keys.next()) {
                            key = keys.getInt(1);
                        }
                    }
                    note.setContentPath(note.getContentPath() + currentCreatorUser.getId() + "/" + key + "/1/" + note.getTitle().replaceAll(" ", "") + ".txt");
                    note.setVersion(1);
                    File noteFile = new File(note.getContentPath());
                    File noteFileParent = noteFile.getParentFile();
                    if (!noteFileParent.exists() && !noteFileParent.mkdirs()) {
                        throw new DataLayerException("An Unexpected Error Occurred! :(");
                        /* throw new DataLayerException("File System Error ( Couldn't create directory " + noteFileParent + " ) - SimplestNoteDataLayerMysqlImpl.storeNote(Note note)"); - DEBUG ONLY */
                    }
                    FileWriter noteFileWriter = new FileWriter(noteFile);
                    try (BufferedWriter noteBufferedWriter = new BufferedWriter(noteFileWriter)) {
                        noteBufferedWriter.write(note.getContentFile());
                    }
                    insertNoteDynamic.setInt(1, key);
                    insertNoteDynamic.setInt(2, note.getVersion());
                    insertNoteDynamic.setString(3, note.getTitle());
                    insertNoteDynamic.setString(4, note.getContentPath());
                    insertNoteDynamic.executeUpdate();
                }
                List<User> storedCreatorUsers;
                User storedCreatorUser = ((storedCreatorUsers = getUsersFromNote(note, CREATOR_PERMISSION, CONFIRMED_STATUS, READ_ACCESS)).size() > 0)? storedCreatorUsers.get(0) : null;
                List<User> storedReadOnlyUsers = getUsersFromNote(note, READONLY_PERMISSION, CONFIRMED_STATUS, READ_ACCESS);
                List<User> storedReadWriteUsers = getUsersFromNote(note, READWRITE_PERMISSION, CONFIRMED_STATUS, READ_ACCESS);
                List<User> storedPendingUsers = getUsersFromNote(note, ALL_PERMISSIONS, PENDING_STATUS, READ_ACCESS);
                List<Tag> storedTags = getTagsFromNote(note, READ_ACCESS); 
                List<User> currentReadOnlyUsers = note.getReadOnlyUsers(READ_ACCESS);
                List<User> currentPendingUsers = note.getPendingUsers(READ_ACCESS);
                List<User> currentReadWriteUsers = note.getReadWriteUsers(READ_ACCESS);
                List<Tag> currentTags = note.getNoteTags(READ_ACCESS);
                if (currentCreatorUser.getId() == 0) { 
                    throw new DataLayerException("An Unexpected Error Occurred! :(");
                    /* throw new DataLayerException("Creator User Error - SimplestNoteDataLayerMysqlImpl.storeNote(Note note)"); */
                } else {
                    if (null != storedCreatorUser) {
                        if (storedCreatorUser.getId() != currentCreatorUser.getId()) {
                            deleteUserParticipationToNote.setInt(1, storedCreatorUser.getId());
                            deleteUserParticipationToNote.setInt(2, key);
                            deleteUserParticipationToNote.executeUpdate();
                            if(getUserNotePermission(currentCreatorUser, note) < 0){
                                updateUserNote.setInt(1, currentCreatorUser.getId());
                                updateUserNote.setInt(2, key);
                                updateUserNote.setInt(3, CREATOR_PERMISSION);
                                updateUserNote.setInt(4, CONFIRMED_STATUS);
                                updateUserNote.executeUpdate();
                            } else {
                                insertUserNote.setInt(1, currentCreatorUser.getId());
                                insertUserNote.setInt(2, key);
                                insertUserNote.setInt(3, CREATOR_PERMISSION);
                                insertUserNote.setInt(4, CONFIRMED_STATUS);
                                insertUserNote.executeUpdate();
                            }
                        }
                    } else { 
                        if(getUserNotePermission(currentCreatorUser, note) > 0){
                            updateUserNote.setInt(1, currentCreatorUser.getId());
                            updateUserNote.setInt(2, key);
                            updateUserNote.setInt(3, CREATOR_PERMISSION);
                            updateUserNote.setInt(4, CONFIRMED_STATUS);
                            updateUserNote.executeUpdate();
                        } else {
                            insertUserNote.setInt(1, currentCreatorUser.getId());
                            insertUserNote.setInt(2, key);
                            insertUserNote.setInt(3, CREATOR_PERMISSION);
                            insertUserNote.setInt(4, CONFIRMED_STATUS);
                            insertUserNote.executeUpdate();
                        }
                    }
                }
                Iterator<User> currentReadOnlyUsersIterator = currentReadOnlyUsers.iterator();
                Iterator<User> storedReadOnlyUsersIterator = storedReadOnlyUsers.iterator();
                while(currentReadOnlyUsersIterator.hasNext()){
                    User currentReadOnlyUser = currentReadOnlyUsersIterator.next();
                    while(storedReadOnlyUsersIterator.hasNext()){
                        User storedReadOnlyUser = storedReadOnlyUsersIterator.next();
                        if(currentReadOnlyUser.getId() == storedReadOnlyUser.getId()){
                            currentReadOnlyUsersIterator.remove();
                            storedReadOnlyUsersIterator.remove();
                            break;
                        }
                    }
                    storedReadOnlyUsersIterator = storedReadOnlyUsers.iterator();
                }
                for(User currentReadOnlyUser : currentReadOnlyUsers){
                    int userPermissionToNote = this.getUserNotePermission(currentReadOnlyUser, note);
                    if(userPermissionToNote < 0){
                        updateUserNote.setInt(1, READONLY_PERMISSION);
                        updateUserNote.setInt(2, CONFIRMED_STATUS);
                        updateUserNote.setInt(3, currentReadOnlyUser.getId());
                        updateUserNote.setInt(4, key);
                        updateUserNote.executeUpdate();
                    } else {
                        insertUserNote.setInt(1, currentReadOnlyUser.getId());
                        insertUserNote.setInt(2, key);
                        insertUserNote.setInt(3, READONLY_PERMISSION);
                        insertUserNote.setInt(4, CONFIRMED_STATUS);
                        insertUserNote.executeUpdate();
                    }
                }
                for(User storedReadOnlyUser : storedReadOnlyUsers){
                    deleteUserParticipationToNote.setInt(1, storedReadOnlyUser.getId());
                    deleteUserParticipationToNote.setInt(2, key);
                    deleteUserParticipationToNote.executeUpdate();
                }
                Iterator<User> storedReadWriteUsersIterator = storedReadWriteUsers.iterator();
                Iterator<User> currentReadWriteUsersIterator = currentReadWriteUsers.iterator();
                while(currentReadWriteUsersIterator.hasNext()){
                    User currentReadWriteUser = currentReadWriteUsersIterator.next();
                    while(storedReadWriteUsersIterator.hasNext()){
                        User storedReadWriteUser = storedReadWriteUsersIterator.next();
                        if(currentReadWriteUser.getId() == storedReadWriteUser.getId()){
                            currentReadWriteUsersIterator.remove();
                            storedReadWriteUsersIterator.remove();
                            break;
                        }
                    }
                    storedReadWriteUsersIterator = storedReadWriteUsers.iterator();
                }
                for(User currentReadWriteUser : currentReadWriteUsers){
                    int userPermission = getUserNotePermission(currentReadWriteUser, note);
                    if(userPermission < 0){
                        updateUserNote.setInt(1, READWRITE_PERMISSION);
                        updateUserNote.setInt(2, CONFIRMED_STATUS);
                        updateUserNote.setInt(3, currentReadWriteUser.getId());
                        updateUserNote.setInt(4, key);
                        updateUserNote.executeUpdate();
                    } else {
                        insertUserNote.setInt(1, currentReadWriteUser.getId());
                        insertUserNote.setInt(2, key);
                        insertUserNote.setInt(3, READWRITE_PERMISSION);
                        insertUserNote.setInt(4, CONFIRMED_STATUS);
                        insertUserNote.executeUpdate();
                    }
                }
                for(User storedReadWriteUser : storedReadWriteUsers){
                    deleteUserParticipationToNote.setInt(1, storedReadWriteUser.getId());
                    deleteUserParticipationToNote.setInt(2, key);
                    deleteUserParticipationToNote.executeUpdate();
                }
                Iterator<User> storedPendingUsersIterator = storedPendingUsers.iterator();
                Iterator<User> currentPendingUsersIterator = currentPendingUsers.iterator();
                while(currentPendingUsersIterator.hasNext()){
                    User currentPendingUser = currentPendingUsersIterator.next();
                    while(storedPendingUsersIterator.hasNext()){
                        User storedPendingUser = storedPendingUsersIterator.next();
                        if(currentPendingUser.getId() == storedPendingUser.getId()){
                            currentPendingUsersIterator.remove();
                            storedPendingUsersIterator.remove();
                            break;
                        }
                    }
                    storedPendingUsersIterator = storedPendingUsers.iterator();
                }
                for(User currentPendingUser : currentPendingUsers){
                    int userPermission = getUserNotePermission(currentPendingUser, note);
                    if(userPermission < 0){
                        updateUserNote.setInt(1, note.getPendingUserPermission(currentPendingUser.getEmailAddress()));
                        updateUserNote.setInt(2, PENDING_STATUS);
                        updateUserNote.setInt(3, currentPendingUser.getId());
                        updateUserNote.setInt(4, key);
                        updateUserNote.executeUpdate();
                    } else {
                        insertUserNote.setInt(1, currentPendingUser.getId());
                        insertUserNote.setInt(2, key);
                        insertUserNote.setInt(3, note.getPendingUserPermission(currentPendingUser.getEmailAddress()));
                        insertUserNote.setInt(4, PENDING_STATUS);
                        insertUserNote.executeUpdate();
                    }
                }
                for(User storedPendingUser : storedPendingUsers){
                    deleteUserParticipationToNote.setInt(1, storedPendingUser.getId());
                    deleteUserParticipationToNote.setInt(2, key);
                    deleteUserParticipationToNote.executeUpdate();
                }
                Iterator<Tag> storedTagsIterator = storedTags.iterator();
                Iterator<Tag> currentTagsIterator = currentTags.iterator();
                while(currentTagsIterator.hasNext()){
                    Tag currentTag = currentTagsIterator.next();
                    while(storedTagsIterator.hasNext()){
                        Tag storedTag = storedTagsIterator.next();
                        if(currentTag.getId() == storedTag.getId()){
                            currentTagsIterator.remove();
                            storedTagsIterator.remove();
                            break;
                        }
                    }
                    storedTagsIterator = storedTags.iterator();
                }
                for(Tag storedTag : storedTags){
                    if(getTagInclusionsNumber(storedTag) > 1){
                        deleteSingleTagFromNoteVersion.setInt(1, storedTag.getId());
                        deleteSingleTagFromNoteVersion.setInt(2, key);
                        deleteSingleTagFromNoteVersion.setInt(3, note.getVersion());
                        deleteSingleTagFromNoteVersion.executeUpdate();
                    } else {
                        storedTag = getTagByKey(storedTag.getId(), WRITE_ACCESS);
                        this.deleteTag(storedTag);
                    }
                }
                for(Tag currentTag : currentTags){
                    insertNoteTag.setInt(1, currentTag.getId());
                    insertNoteTag.setInt(2, note.getId());
                    insertNoteTag.setInt(3, note.getVersion());
                    insertNoteTag.executeUpdate();
                } 
                if (key > 0) {
                    note.copyFrom(getNoteByKey(key, note.getVersion(), note.getAccessMode()));
                }
                note.setDirty(false);
            } catch (SQLException | IOException ex) {
                throw new DataLayerException("An Unexpected Error Occurred! :(");
                /* throw new DataLayerException("SQLException - SimplestNoteDataLayerMysqlImpl.storeNote(Note note)", ex); */
            } finally {
                try {
                    if (keys != null) {
                        keys.close();
                    }
                    if (note.getAccessMode() == WRITE_ACCESS) {
                        note.setAccessMode(READ_ACCESS);
                        connection.commit();
                        connection.setAutoCommit(true);
                    }
                } catch (SQLException ex) {/* Nothing Done */}
            }
        } else {
            throw new DataLayerException("An Unexpected Error Occurred! :(");
            /* throw new DataLayerException("Null Note Instance - SimplestNoteDataLayerMysqlImpl.storeNote(Note note)"); - DEBUG ONLY */
        }
    }
    
    @Override
    public int storeTag(Tag tag) throws DataLayerException {
        if(null != tag){
            ResultSet keys = null;
            int key = tag.getId();
            try {
                if (key > 0) {
                    if (!tag.isDirty()) {
                        return tag.getId();
                    }
                    updateTag.setString(1, tag.getContent());
                    updateTag.setInt(2, tag.getId());
                    updateTag.executeUpdate();
                } else {
                    insertTag.setString(1, tag.getContent());
                    if (insertTag.executeUpdate() == 1) {
                        keys = insertTag.getGeneratedKeys();
                        if (keys.next()) {
                            key = keys.getInt(1);
                        }
                    }
                }
                return key;
            } catch (SQLException ex) {
                throw new DataLayerException("An Unexpected Error Occurred! :(");
                /* throw new DataLayerException("SQLException - SimplestNoteDataLayerMysqlImpl.storeTag(Tag tag)", ex); - DEBUG ONLY */
            } finally {
                try {
                    if (keys != null) {
                        keys.close();
                    }
                    if (tag.getAccessMode() == WRITE_ACCESS) {
                        tag.setAccessMode(READ_ACCESS);
                        connection.commit();
                        connection.setAutoCommit(true);
                    }
                } catch (SQLException ex) {/* Nothing Done */}
            }
        } else {
            throw new DataLayerException("An Unexpected Error Occurred! :(");
            /* throw new DataLayerException("Null Tag Instance - SimplestNoteDataLayerMysqlImpl.storeTag(Tag tag)"); - DEBUG ONLY */
        }
    }

    @Override
    public void deleteUser(User user) throws DataLayerException {
        try {
            if(null == user){
                throw new DataLayerException("An Unexpected Error Occurred! :(");
                /* throw new DataLayerException("Null User Instance - SimplestNoteDataLayerMysqlImpl.deleteUser(User user)"); */
            }
            boolean first = true;
            String userFolderPath = "";
            List<Note> storedCreatedNotes = getNotesFromUser(user, CREATOR_PERMISSION, CONFIRMED_STATUS, READ_ACCESS); 
            deleteAllUserParticipationsToNotes.setInt(1, user.getId());
            deleteAllUserParticipationsToNotes.executeUpdate();
            for (Note storedCreatedNote : storedCreatedNotes) {
                if(first){
                    userFolderPath = storedCreatedNote.getContentPath().substring(0, storedCreatedNote.getContentPath().lastIndexOf("/"));
                    first = false;
                }
                deleteNote(storedCreatedNote);
            }
            if(first && userFolderPath.isEmpty()){
                userFolderPath = user.getFirstName() + "../../users/" + user.getId();
            } else {
                while(!userFolderPath.isEmpty() && !(userFolderPath.substring(0, userFolderPath.lastIndexOf("/"))).substring((userFolderPath.substring(0, userFolderPath.lastIndexOf("/"))).lastIndexOf("/")).equals("/users")){ 
                    userFolderPath = userFolderPath.substring(0, userFolderPath.lastIndexOf("/"));
                }
            }
            if(!delFileRecursively(new File(userFolderPath))){
                throw new DataLayerException("An Unexpected Error Occurred! :(");
                /* throw new DataLayerException("File System Error - SimplestNoteDataLayerMysqlImpl.deleteUser(User user)"); */
            }
            deleteUser.setInt(1, user.getId());
            deleteUser.executeUpdate();
        } catch (SQLException ex) {
            throw new DataLayerException("An Unexpected Error Occurred! :(");
            /* throw new DataLayerException("SQLException - SimplestNoteDataLayerMysqlImpl.deleteUser(User user)", ex); */
        } finally {
            try {
                if (null != user && user.getAccessMode() == WRITE_ACCESS) {
                    user.setAccessMode(READ_ACCESS);
                    connection.commit();
                    connection.setAutoCommit(true);
                }
            } catch (SQLException ex) {/* Nothing Done */}
        }
    }

    @Override
    public void deleteNoteVersion(Note note) throws DataLayerException {
        try {
            if (null == note) {
                throw new DataLayerException("An Unexpected Error Occurred! :(");
                /* throw new DataLayerException("Null Note Instance Error - SimplestNoteDataLayerMysqlImpl.deleteNoteVersion(Note note)"); - DEBUG ONLY */
            }
            File noteVersionFolder = new File(note.getContentPath().substring(0, note.getContentPath().lastIndexOf("/")));
            if(!delFileRecursively(noteVersionFolder)){
                throw new DataLayerException("An Unexpected Error Occurred! :(");
                /* throw new DataLayerException("File System Error - SimplestNoteDataLayerMysqlImpl.deleteNoteVersion(Note note)"); - DEBUG ONLY */
            }
            deleteNoteVersion.setInt(1, note.getId());
            deleteNoteVersion.setInt(2, note.getVersion());
            deleteNoteVersion.executeUpdate();
            note.setContentPath((note.getContentPath().substring(0, note.getContentPath().lastIndexOf("/"))).substring(0, (note.getContentPath().substring(0,note.getContentPath().lastIndexOf("/"))).lastIndexOf("/")) + "/");
        } catch (SQLException ex) {
            throw new DataLayerException("An Unexpected Error Occurred! :(");
            /* throw new DataLayerException("SQLException - SimplestNoteDataLayerMysqlImpl.deleteNoteVersion(Note note)", ex); - DEBUG ONLY */
        }
    }

    @Override
    public void deleteNote(Note note) throws DataLayerException {
        try {
            if (null == note) {
                throw new DataLayerException("An Unexpected Error Occurred! :(");
                /* throw new DataLayerException("Null Note Instance Error - SimplestNoteDataLayerMysqlImpl.deleteNote(Note note)"); - DEBUG ONLY */
            }
            List<Tag> storedTags = getTagsFromNote(note, READ_ACCESS); 
            deleteAllTagsFromNoteVersions.setInt(1, note.getId());
            deleteAllTagsFromNoteVersions.executeUpdate();
            for (Tag tag : storedTags) {
                if (getTagInclusionsNumber(tag) == 1) { 
                    deleteTag(tag);
                }
            }
            deleteAllUsersParticipationToNote.setInt(1, note.getId());
            deleteAllUsersParticipationToNote.executeUpdate();
            List<Note> noteHistory = note.getNoteHistory(READ_ACCESS);
            for(Note noteVersion : noteHistory){
                this.deleteNoteVersion(noteVersion);
            }
            this.deleteNoteVersion(note);
            String noteFolderPath = note.getContentPath();
            for(int i = 0; i < 2; i++){
                noteFolderPath = noteFolderPath.substring(0, noteFolderPath.lastIndexOf("/"));
            }
            File noteFolder = new File(noteFolderPath + "/");
            if(!delFileRecursively(noteFolder)){
                throw new DataLayerException("An Unexpected Error Occurred! :(");
                /* throw new DataLayerException("File System Error - SimplestNoteDataLayerMysqlImpl.deleteNote(Note note)"); - DEBUG ONLY */
            }
            deleteNoteStatic.setInt(1, note.getId());
            deleteNoteStatic.executeUpdate();
        } catch (SQLException ex) {
            throw new DataLayerException("An Unexpected Error Occurred! :(");
            /* throw new DataLayerException("SQLException - SimplestNoteDataLayerMysqlImpl.deleteNote(Note note)", ex); */
        } finally {
            try {
                if (null != note && note.getAccessMode() == WRITE_ACCESS) {
                    note.setAccessMode(READ_ACCESS);
                    connection.commit();
                    connection.setAutoCommit(true);
                }
            } catch (SQLException ex) {/* Nothing Done */}
        }
    }

    @Override
    public void deleteTag(Tag tag) throws DataLayerException {
        try {
            if (null == tag) {
                throw new DataLayerException("An Unexpected Error Occurred! :(");
                /* throw new DataLayerException("Null Tag Instance Error - SimplestNoteDataLayerMysqlImpl.deleteTag(Tag tag)"); - DEBUG ONLY */
            }
            deleteSingleTagFromAllNotesVersions.setInt(1, tag.getId());
            deleteSingleTagFromAllNotesVersions.executeUpdate();
            deleteTag.setInt(1, tag.getId());
            deleteTag.executeUpdate();
        } catch (SQLException ex) {
            throw new DataLayerException("An Unexpected Error Occurred! :(");
            /* throw new DataLayerException("SQLException - SimplestNoteDataLayerMysqlImpl.deleteTag(Tag tag)", ex); - DEBUG ONLY */
        } finally {
            try {
                if (null != tag && tag.getAccessMode() == WRITE_ACCESS) {
                    tag.setAccessMode(READ_ACCESS);
                    connection.commit();
                    connection.setAutoCommit(true);
                }
            } catch (SQLException ex) {/* Nothing Done */}
        }
    }
    
}
