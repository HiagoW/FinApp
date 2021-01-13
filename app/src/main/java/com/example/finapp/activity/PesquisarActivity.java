package com.example.finapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finapp.R;
import com.example.finapp.Utils;
import com.example.finapp.helper.CategoriaDAO;
import com.example.finapp.helper.OperacaoDAO;
import com.example.finapp.model.Categoria;
import com.example.finapp.model.Operacao;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PesquisarActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    Spinner spinner;
    TextView textViewData1, textViewData2, textViewSpinner;
    Categoria categoria;
    OperacaoDAO operacaoDAO;
    String data1, data2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisar);
        spinner = findViewById(R.id.spinner);
        textViewData1 = findViewById(R.id.textView9);
        textViewData2 = findViewById(R.id.textView10);

        CategoriaDAO categoriaDAO = new CategoriaDAO(getApplicationContext());
        operacaoDAO = new OperacaoDAO(getApplicationContext());

        List<Categoria> categorias = categoriaDAO.getAllCategorias();
        List<Operacao> operacoes = operacaoDAO.getAllOperacoes();

        ArrayAdapter categoriaAdapter = new ArrayAdapter(this,R.layout.spinner,categorias);
        spinner.setAdapter(categoriaAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                categoria = (Categoria) adapterView.getSelectedItem();
                textViewSpinner = findViewById(R.id.textViewSpinner);
                if (categoria.isDebito() == 1) {
                    textViewSpinner.setTextColor(Color.parseColor("#ff0000"));
                } else {
                    textViewSpinner.setTextColor(Color.parseColor("#00ff00"));
                }
                Toast.makeText(PesquisarActivity.this, "Selecionado: " + categoria, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    public void pesquisar(View view) throws ParseException {
        if(data1==null){
            Toast.makeText(PesquisarActivity.this,"Selecione uma data. ",Toast.LENGTH_SHORT).show();
            return ;
        }
        if(categoria==null){
            Toast.makeText(PesquisarActivity.this,"Selecione uma categoria. ",Toast.LENGTH_SHORT).show();
            return ;
        }
        Date date;
        try{
            date = Utils.stringToDate(data1);
        }catch (Exception e){
            Toast.makeText(PesquisarActivity.this,"Selecione uma data válida. ",Toast.LENGTH_SHORT).show();
            return ;
        }

    }
    public void datePicker(View view){
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }


    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        data1 = day+"/"+(month+1)+"/"+year;
        textViewData1.setText(data1);
    }


}