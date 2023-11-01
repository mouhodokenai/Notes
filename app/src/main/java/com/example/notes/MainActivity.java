package com.example.notes;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity  {

    private ListView listView;
    private ArrayList<Note> listOfNotes;
    private ArrayAdapter<Note> noteAdapter;

    // Запуск активности для получения результата
    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    int resultCode = result.getResultCode();
                    Intent data = result.getData();

                    if (resultCode == Activity.RESULT_OK && data != null) {
                        int updatedNoteId = data.getIntExtra("id", -1);
                        Note updatedNote = (Note) data.getSerializableExtra("updatedNote");

                        if (updatedNoteId != -1 && updatedNote != null) {
                            try (ObjectOutputStream objOut = new ObjectOutputStream(openFileOutput(updatedNoteId + "note.txt", MODE_PRIVATE))) {
                                objOut.writeObject(updatedNote);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                            // Обновление списка listOfNotes
                            listOfNotes.set(updatedNoteId, updatedNote);

                            // Обновление адаптера
                            listView.setAdapter(noteAdapter);
                        }
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.list);
        listOfNotes = new ArrayList<Note>();

        try {
            // Попытка чтения сохраненных заметок
            Note note1 = ReadSave(0);
            Note note2 = ReadSave(1);
            Note note3 = ReadSave(2);
            Note note4 = ReadSave(3);
            Note note5 = ReadSave(4);
            Note note6 = ReadSave(5);

            // Добавление прочитанных заметок в список
            listOfNotes.add(note1);
            listOfNotes.add(note2);
            listOfNotes.add(note3);
            listOfNotes.add(note4);
            listOfNotes.add(note5);
            listOfNotes.add(note6);
        } catch (Exception e) {
            // Если чтение не удалось, создаем и добавляем новые заметки
            Note note1 = new Note(1, "тема1", "текст 1");
            Note note2 = new Note(1, "тема2", "текст 2");
            Note note3 = new Note(1, "тема3", "текст 3");
            Note note4 = new Note(1, "тема4", "текст 4");
            Note note5 = new Note(1, "тема5", "текст 5");
            Note note6 = new Note(1, "тема6", "текст 6");

            // Добавление новых заметок в список
            listOfNotes.add(note1);
            listOfNotes.add(note2);
            listOfNotes.add(note3);
            listOfNotes.add(note4);
            listOfNotes.add(note5);
            listOfNotes.add(note6);
        }

        // Сохранение заметок в файлы
        for (int i = 0; i < listOfNotes.size(); i++) {
            try (ObjectOutputStream objOut = new ObjectOutputStream(openFileOutput(i + "note.txt", MODE_PRIVATE))) {
                objOut.writeObject(listOfNotes.get(i));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        noteAdapter = new ArrayAdapter<Note>(this, android.R.layout.simple_list_item_1, listOfNotes);
        listView.setAdapter(noteAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // получаем выбранный элемент
                Note selectedNote = listOfNotes.get(i);
                // переходим к второй активности и передаем данные
                Intent intent = new Intent(MainActivity.this, Activity2.class);
                intent.putExtra("id", i);
                // startActivity(intent);
                mStartForResult.launch(intent);
            }
        });
    }

    // Метод для чтения сохраненных заметок из файла
    Note ReadSave(int num) {
        Note note;
        try (FileInputStream fileIn = openFileInput(num + "note.txt")) {
            ObjectInputStream in = new ObjectInputStream(fileIn);
            note = (Note) in.readObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return note;
    }
}
