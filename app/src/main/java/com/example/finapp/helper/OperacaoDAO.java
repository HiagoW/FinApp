package com.example.finapp.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.finapp.model.Categoria;
import com.example.finapp.model.Operacao;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OperacaoDAO {

    private SQLiteDatabase read;
    private SQLiteDatabase write;

    public OperacaoDAO(Context context){
        DBHelper db = new DBHelper(context);
        read = db.getReadableDatabase();
        write = db.getWritableDatabase();
    }

    public List<Operacao> getAllOperacoes() {
        List<Operacao> operacaoList = new ArrayList<>();
        try{
            String sql = "SELECT o.id, o.valor, o.data, o.categoria, c.id, c.descricao, c.debito "+
                    "FROM " + DBHelper.TABLE1_NAME + " o JOIN " + DBHelper.TABLE2_NAME +" c " +
                    "ON(o.categoria=c.id) ";
            Cursor cursor = read.rawQuery(sql,null);
            while(cursor.moveToNext()){
                Operacao op = new Operacao();
                Long id = cursor.getLong(0);
                Double valor = cursor.getDouble(1);
                long miliseconds = cursor.getLong(2);
                Date data = new Date(miliseconds);
                op.setId(id);
                op.setValor(valor);
                op.setData(data);
                Categoria categoria = new Categoria();
                Long idCat = cursor.getLong(4);
                String descricao = cursor.getString(5);
                int debito = cursor.getInt(6);
                op.setCategoria(categoria);
                categoria.setId(idCat);
                categoria.setDescricao(descricao);
                categoria.setDebito(debito);
                operacaoList.add(op);
            }
            return operacaoList;
        }catch (Exception e){
            return null;
        }
    }

    public boolean insertOperacao(Operacao operacao){
        ContentValues values = new ContentValues();
        long miliseconds = operacao.getData().getTime();
        values.put("data",miliseconds);
        values.put("valor",operacao.getValor());
        values.put("categoria",operacao.getCategoria().getId());
        try{
            write.insert(DBHelper.TABLE1_NAME,null,values);
            Log.i("INFO", "Tarefa salva.");
        }catch (Exception e){
            Log.e("INFO","Erro ao salvar" + e.getMessage());
            return false;
        }
        return true;
    }
}
