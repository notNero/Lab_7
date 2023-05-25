package com.topic2.android.notes.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import com.topic2.android.notes.domain.model.NoteModel
import com.topic2.android.notes.ui.components.Note
import com.topic2.android.notes.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@Composable
private fun NotesList(
    notes: List<NoteModel>,
    onNoteCheckedChange: (NoteModel) -> Unit,
    onNoteClick: (NoteModel) -> Unit
) {
    LazyColumn {
        items(count = notes.size){noteIndex->
            val note = notes[noteIndex]
            Note(
                note = note,
                onNoteClick = onNoteClick,
                onNoteCheckedChange = onNoteCheckedChange,,
            )
        }
    }
}

@Preview
@Composable
private fun NotesListPreview() {
    NotesList(
        notes = listOf(
            NoteModel(1, "Note 1", "Content 1", null),
            NoteModel(1, "Note 2", "Content 2", false),
            NoteModel(1, "Note 3", "Content 3", true),
        ),
        onNoteCheckedChange = {},
        onNoteClick = {}
    )
}
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun NotesScreen(viewModel: MainViewModel) {

    val notes: List<NoteModel> by viewModel
        .notesNotInTrash
        .observeAsState(listOfNotNull())
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Notes",
                        color = MaterialTheme.colors.onPrimary
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                scaffoldState.drawerState.open()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.List,
                            contentDescription = "Drawer Button"
                        )
                    }
                }
            )
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onCreateNewNoteClick() },
                contentColor = MaterialTheme.colors.background,
                content = {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add Note Button"
                    )
                }
            )
        },
        content = {
            if (notes.isNotEmpty()) {
                NotesList(
                    notes = notes,
                    onNoteCheckedChange = { viewModel.onNoteCheckedChange(it)
                    },
                    onNoteClick = { viewModel.onNoteClick(it) }
                )
            }
        }
    )
}