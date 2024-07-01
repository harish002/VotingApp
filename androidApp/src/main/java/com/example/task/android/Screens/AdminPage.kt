package com.example.task.android.Screens

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.json.JSONObject


@Composable
fun admin() {
    val context = LocalContext.current
    val voteList = getVotes(context).collectAsState(initial = emptyList())


    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Vote Count")

        Spacer(modifier = Modifier.padding(40.dp))

        LazyColumn(){
            Log.d("test",voteList.value.toString())
            items(voteList.value.size){i->
                Text(text = "${voteList.value[0]}")
            }
        }

    }
}

@Composable
fun getVotes(context: Context): StateFlow<List<String>> {
    val _voteList = remember { MutableStateFlow<List<String>>(emptyList()) }

    LaunchedEffect(Unit) {
        val db = FirebaseFirestore.getInstance()
        db.collection("Vote")
            .get()
            .addOnSuccessListener { queryDocumentSnapshots ->
                if (!queryDocumentSnapshots.isEmpty) {
                    val list = queryDocumentSnapshots.documents
                    val tempVoteList = mutableListOf<String>()
                    for (d in list) {
                        tempVoteList.add(d.data.toString())
                    }
                    _voteList.value = tempVoteList
                } else {
                    Toast.makeText(
                        context,
                        "No data found in Database",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Fail to get the data. $e", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    return _voteList
}







