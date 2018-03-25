package it.univaq.f4i.iw.simplestnote.data.impl;

/**
 *
 * @author Lorenzo Addazi (addazi.lorenzo@gmail.com)
 */
public class SimplestNoteDataLayerMysqlImplQueries {
    
    /* SHARED LOCK PLUGIN */
        public static final String forUpdate = " FOR UPDATE ";
        
    /* NOTE STATUS PLUGIN */
        public static final String andStatus = " AND user_note.status=? ";
    /* NOTE PERMISSION PLUGIN */
        public static final String andPermission = " AND user_note.permission=? ";
    
    /* USER QUERIES */
        /* SELECT */
        public static final String selectUserByIdQuery = "SELECT * FROM user WHERE id=? ";
        public static final String selectUsersByEmailAddressQuery = "SELECT user.id FROM user WHERE email_address like ? ";
        public static final String selectUsersFromNoteQuery = "SELECT user.id FROM user_note JOIN user ON user.id = user_note.user_id WHERE user_note.note_id = ? ";
        /* INSERT */
        public static final String insertUserQuery = "INSERT INTO user(first_name, last_name, email_address, password) VALUES (?,?,?,?) ";
        /* UPDATE */
        public static final String updateUserQuery = "UPDATE user SET first_name=?, last_name=?, email_address=?, password=? WHERE id=? ";
        /* DELETE */
        public static final String deleteUserQuery = "DELETE FROM user WHERE id=? ";
    
    /* USER NOTE QUERIES */
        /* SELECT */
        public static final String selectUserParticipationPermissionQuery = "SELECT permission FROM user_note WHERE user_id=? AND note_id=? ";
        /* INSERT */
        public static final String insertUserNoteQuery = "INSERT INTO user_note(user_id, note_id, permission, status) VALUES (?,?,?,?)";
        /* UPDATE */
        public static final String updateUserNoteQuery = "UPDATE user_note SET permission=?, status=? WHERE user_id=? AND note_id=? ";
        /* DELETE */
        public static final String deleteUserParticipationToNoteQuery = "DELETE FROM user_note WHERE user_id=? ";
        public static final String deleteUsersParticipationsFromNoteQuery = "DELETE FROM user_note WHERE note_id=? ";
        public static final String withNote = "AND note_id=? ";
        
    /* NOTE QUERIES */
        /* SELECT */
        public static final String selectNoteByIdAndVersionQuery = "SELECT NS.id, ND.version, ND.title, ND.content, NS.last_update FROM note_dynamic ND join note_static NS ON NS.id = ND.note_id WHERE ND.note_id = NS.id AND NS.id = ? AND ND.version = ? ";
        public static final String selectNotesByTitleQuery = "SELECT NS.id, ND.version FROM note_dynamic AS ND join note_static AS NS ON NS.id = ND.note_id WHERE (ND.note_id, ND.version) IN (SELECT note_id, MAX(version) FROM note_dynamic GROUP BY note_id ) AND title LIKE ? ";
        public static final String selectNotesFromUserQuery = "SELECT NC.id, NC.version FROM (SELECT NS.id, ND.version, ND.title, ND.content, NS.last_update FROM note_static AS NS JOIN note_dynamic ND on NS.id = ND.note_id) NC JOIN user_note ON NC.id = user_note.note_id WHERE user_note.user_id = ? AND (note_id, version) IN (SELECT note_id, MAX(version) FROM note_dynamic GROUP BY note_id ) ";
        public static final String selectNotesFromTagQuery = "SELECT NC.* FROM (SELECT NS.id, ND.version FROM note_static NS JOIN note_dynamic ND ON NS.id = ND.note_id) NC JOIN note_tag NT ON (NT.note_id = NC.id AND NT.note_version = NC.version) WHERE (NC.id, NC.version) IN (SELECT note_id, MAX(version) FROM note_dynamic GROUP BY note_id ) AND NT.tag_id = ? ";
        public static final String selectNoteHistoryQuery = "SELECT NC.* FROM (SELECT NS.id, ND.version FROM note_static NS JOIN note_dynamic ND ON NS.id = ND.note_id) NC WHERE id = ? AND NC.version <> ( SELECT MAX(version) AS latest_version FROM note_dynamic WHERE note_id=? )";
        public static final String selectLatestNoteVersionQuery = "SELECT MAX(version) AS latest_version FROM note_dynamic WHERE note_id=? ";
        public static final String selectAllNotesVersionsQuery = "SELECT version FROM note_dynamic WHERE note_id=? ";
        /* INSERT */
        public static final String insertNoteStaticQuery = "INSERT INTO note_static(last_update) VALUES (now()) ";
        public static final String insertNoteDynamicQuery = "INSERT INTO note_dynamic(note_id, version, title, content) VALUES (?,?,?,?) ";
        /* UPDATE */
        public static final String updateNoteStaticQuery = "UPDATE note_static SET last_update=now() WHERE id=? ";
        public static final String updateNoteDynamicQuery = "UPDATE note_dynamic SET title=?, content=? WHERE note_id=? AND version=? ";
        /* DELETE */
        public static final String deleteNoteStaticQuery = "DELETE FROM note_static WHERE id=? ";
        public static final String deleteNoteDynamicQuery = "DELETE FROM note_dynamic WHERE note_id=? ";
        public static final String withVersion = " AND version=? ";
        public static final String deleteNoteHistoryQuery = "DELETE FROM note_dynamic WHERE (note_dynamic.version) NOT IN (SELECT MAX(MDC.version) FROM (SELECT note_dynamic.* FROM note_dynamic JOIN note_static ) MDC WHERE MDC.note_id = ?) AND note_id = ? ";
    
    /* NOTE TAG QUERIES */
        /* SELECT */
        public static final String selectTagInclusionsNumberQuery = "SELECT COUNT(*) AS tag_inclusions_number FROM note_tag WHERE tag_id=? ";
        /* INSERT */
        public static final String insertNoteTagQuery = "INSERT INTO note_tag(tag_id, note_id, note_version) VALUES(?,?,?) ";
        /* DELETE */
        public static final String deleteTagFromNoteQuery = "DELETE FROM note_tag ";
        public static final String fromTag = "WHERE tag_id=? ";
        public static final String fromNote = "WHERE note_id=? ";
        public static final String withNoteId = " AND note_id=? ";
        public static final String withNoteVersion = "AND note_version=? ";
    
    /* TAG QUERIES */
        /* SELECT */
        public static final String selectTagByIdQuery = "SELECT * FROM tag WHERE tag.id=? ";
        public static final String selectTagsByContentQuery = "SELECT tag.id FROM tag WHERE content LIKE ? ";
        public static final String selectTagsFromUserQuery = "SELECT tag.id FROM tag JOIN (SELECT * FROM note_tag JOIN (SELECT NC.* FROM (SELECT NS.id, ND.version, ND.title, ND.content, NS.last_update FROM note_static NS JOIN note_dynamic ND ON NS.id = ND.note_id) NC JOIN user_note ON NC.id = user_note.note_id WHERE user_note.user_id = ? AND (note_id, version) IN (SELECT note_id, MAX(version) FROM note_dynamic GROUP BY note_id )) NN ON (NN.id = note_tag.note_id AND NN.version = note_tag.note_version)) TS on TS.tag_id = tag.id GROUP BY tag_id, tag.content ";
        public static final String selectTagsFromNoteQuery = "SELECT tag.id FROM tag JOIN note_tag ON tag.id = note_tag.tag_id WHERE note_tag.note_id = ? AND note_tag.note_version = ? ";
        /* INSERT */
        public static final String insertTagQuery =  "INSERT INTO tag(content) VALUES(?) ";
        /* UPDATE */
        public static final String updateTagQuery = "UPDATE tag SET content=? WHERE id=? ";
        /* DELETE */
        public static final String deleteTagQuery = "DELETE FROM tag WHERE id=? ";
    
}
