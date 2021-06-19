package cu.uci.example.check.paid.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.util.Log
import cu.uci.example.check.paid.provider.Util.randomString

class ApklisProvider : ContentProvider() {

    private val APKLIS_PAID = "paid"
    private val APKLIS_USER_NAME = "user_name"
    private val APKLIS_USER_NAME_SECURITY = "user_name_security"

    override fun onCreate(): Boolean {
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor {

        val username = randomString(15, Util.USERNAME_VALID_CHARS)
        Log.d("LOL", "provider wit username $username")

        val columns = arrayOf(APKLIS_PAID, APKLIS_USER_NAME, APKLIS_USER_NAME_SECURITY)
        val matrixCursor = MatrixCursor(columns)
        matrixCursor.addRow(arrayOf(1, username, encryptRSA(username)))
        return matrixCursor

    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        return 0
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        return 0
    }

}
