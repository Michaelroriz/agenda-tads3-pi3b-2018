package br.senac.tads.pi3b.agenda;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author michael.aroriz
 */
public class Agenda {

    private static Scanner entrada = new Scanner(System.in);
    
    private Connection obterConexao() throws ClassNotFoundException, SQLException {
        Connection conn = null;
        //Passo 1: Registrar driver JDBC
        Class.forName("com.mysql.jdbc.Driver");
        //Passo 2: Obter conexão com o BD
        conn = (Connection) DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/agendabd",
                "root",
                "");
        return conn;
    }
    //executar comando
    public void executar() {
        String querySql = "SELECT ID, NOME, DTNASCIMENTO FROM PESSOA";

        try (
                Connection conn = obterConexao();
                PreparedStatement stmt = conn.prepareStatement(querySql);
                ResultSet resultados = stmt.executeQuery()) {

            while (resultados.next()) {
                long id = resultados.getLong("ID");
                String nome = resultados.getString("NOME");
                Date dtNascimento = resultados.getDate("DTNASCIMENTO");
                System.out.println(id + " " + nome + " " + dtNascimento);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Agenda.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Agenda.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void inserir() {

        System.out.print("Digite um nome: ");
        String name = entrada.nextLine();
        System.out.print("Digite a data de nascimento no formato dd/mm/aaaa: ");
        String dateN = entrada.nextLine();

        String querySql = "INSERT INTO PESSOA (NOME, DTNASCIMENTO) VALUES (?,?)";

        try {
            Connection conn = obterConexao();
            PreparedStatement stmt = conn.prepareStatement(querySql);
            stmt.setString(1, name);
            DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            Date dataNasc = null;
            try {
                dataNasc = format.parse(dateN);
            } catch (ParseException ex) {
                Logger.getLogger(Agenda.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Data de nascimento invalida");
            }
            stmt.setDate(2, new java.sql.Date(dataNasc.getTime()));
            stmt.executeUpdate();
            System.out.println("Pessoa cadastrada com sucesso");

        } catch (SQLException e) {
            System.out.println("Nao foi possivel executar");
        } catch (ClassNotFoundException e) {
            System.out.println("Nao foi possivel executar");
        }
    }

    public void deletar() {
        System.out.print("Qual ID da pessoa que deseja deletar: ");
        int id = entrada.nextInt();

        String querySql = "DELETE FROM PESSOA WHERE ID =" + id;
        
        try {
            Connection conn = obterConexao();
            PreparedStatement stmt = conn.prepareStatement(querySql);            
            stmt.execute(querySql, id);
        }catch (ClassNotFoundException ex) {
            Logger.getLogger(Agenda.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Agenda.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        Agenda agenda = new Agenda();
        agenda.executar();
        agenda.deletar();
        //agenda.executar();
        int i= 0;
        while(i<100){
            for (int j = 0; j < 10; j++) {
                System.out.println("Nº:" +i);
            }
        }
       
    }
}
