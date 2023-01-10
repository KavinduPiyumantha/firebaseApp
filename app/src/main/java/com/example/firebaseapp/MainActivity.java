package com.example.firebaseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    EditText bookISBN,bookName,bookAuthor,bookPublisher;
    DatabaseReference databaseReference;
    Books book;

    long maxBookID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bookISBN = (EditText) findViewById(R.id.etISBN);
        bookName =(EditText) findViewById(R.id.etName);
        bookAuthor=(EditText) findViewById(R.id.etAuthor);
        bookPublisher=(EditText) findViewById(R.id.etAuthor);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Book");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    maxBookID=snapshot.getChildrenCount();
                    System.out.println("max id "+ maxBookID);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void onAdd(View view){
        String ISBN =bookISBN.getText().toString();
        String name =bookName.getText().toString();
        String author =bookAuthor.getText().toString();
        String publisher = bookPublisher.getText().toString();

        book = new Books();

        book.setISBNNo(ISBN);
        book.setName(name);
        book.setAuthor(author);
        book.setPublisher(publisher);

        databaseReference.child(String.valueOf(maxBookID+1)).setValue(book);

        Toast.makeText(this, "Successfully added",Toast.LENGTH_SHORT).show();

    }

    public void onViewData(View view){
        DatabaseReference childReference;

        for(int i=1;i<=maxBookID;i++){
//            System.out.println("i ;"+ i);
            childReference = FirebaseDatabase.getInstance().getReference().child("Book").child(String.valueOf(i));
            childReference.addValueEventListener(new ValueEventListener() {

                String isbnno;
                String name;
                String author;
                String publisher;
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if(snapshot.exists()) {
                        isbnno = Objects.requireNonNull(snapshot.child("isbnno").getValue(String.class));
                        name = Objects.requireNonNull(snapshot.child("name").getValue(String.class));
                        author = Objects.requireNonNull(snapshot.child("author").getValue(String.class));
                        publisher = Objects.requireNonNull(snapshot.child("publisher").getValue(String.class));

                        System.out.println("ISBN NO :" + isbnno + "\t" + "Name :" + name + "\t" + "Author :" + author + "\t" + "Publisher :" + publisher + "\t");
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }



}