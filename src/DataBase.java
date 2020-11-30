import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;


public class DataBase {
    public DataBase() {
        System.out.println("Data base started ");
        //rest of code
    }
    private Connection connect() {
        String url = "jdbc:sqlite:src/DataBase.db";
        //UNCOMMENT THIS FOR MS ACCES DATABASES
        //String url = "jdbc:ucanaccess://src//DataBaseAccess.accdb";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
    //Add New Book to DataBase
    public void insertBook(int isbn, String title, String author, int numofcopies, int yearpub, String publisher, int numpages) {
        String sql = "INSERT INTO libro (ISBN,titulo,autor,númejemplares,anyopublicacion,editorial,numpag) VALUES(?,?,?,?,?,?,?)";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setInt(1,isbn);
            pstmt.setString(2,title);
            pstmt.setString(3,author);
            pstmt.setInt(4,numofcopies);
            pstmt.setInt(5,yearpub);
            pstmt.setString(6,publisher);
            pstmt.setInt(7,numpages);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    //Add New User to DataBase
    public void insertUser(String firstname, String lastname, int phonenum, int age, java.sql.Date date) {
        String sql = "INSERT INTO socio (nombre,apellidos,telefono,edad,fecha) VALUES (?,?,?,?,?)";



        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
        )
        {
            pstmt.setString(1,firstname);
            pstmt.setString(2,lastname);
            pstmt.setInt(3,phonenum);
            pstmt.setInt(4,age);
            pstmt.setDate(5,date);
            pstmt.executeUpdate();

            /*ResultSet tableKeys = pstmt.getGeneratedKeys();
            tableKeys.next();*/

        }catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }
    //Add New User to DataBase
    public void insertBorrowing(int BookIsbn, int UserNumsocio,java.sql.Date DateBorrowing,java.sql.Date DateReturn) {
        String sql = "INSERT INTO prestamo (libro,socio,fprestamo,fdevolucion) VALUES (?,?,?,?)";
        String defaultdate = "0001-01-01";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
        )
        {
            pstmt.setInt(1,BookIsbn);
            pstmt.setInt(2,UserNumsocio);
            pstmt.setDate(3,DateBorrowing);
            if(DateReturn.compareTo(java.sql.Date.valueOf(defaultdate))==0)
            {
                pstmt.setNull(4,Types.DATE);
                pstmt.executeUpdate();
            }
            else
            {
                pstmt.setDate(4,DateReturn);
                pstmt.executeUpdate();
            }
            /*
            ResultSet tableKeys = pstmt.getGeneratedKeys();
            tableKeys.next();
             */
        }catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }
    //Delete Book
    public void deleteBook(int isbn) {
        String sql = "DELETE FROM libro WHERE ISBN=?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
        )
        {
            pstmt.setInt(1,isbn);
            pstmt.executeUpdate();

        }catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }
//DELETE USER
    public void deleteUser(int numsocio) {
        String sql = "DELETE FROM socio WHERE numsocio=?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
        )
        {
            pstmt.setInt(1,numsocio);
            pstmt.executeUpdate();

        }catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }
    public JTable selectBooks() {
        System.out.println("Executing resultset");
        String sql = "SELECT * FROM libro";
        ResultSet rs;
        JTable table = new JTable();
        DefaultTableModel dtm = new DefaultTableModel();
        dtm.setRowCount(0);
        dtm.addColumn("ISBN");
        dtm.addColumn("TITULO");
        dtm.addColumn("AUTOR");
        dtm.addColumn("NUM. EJEMPLARES");
        dtm.addColumn("YEAR PUBLICACION");
        dtm.addColumn("EDITORIAL");
        dtm.addColumn("NUM. PAGINAS");
        table.setModel(dtm);
        Object[] data = new Object[7];
        try {
            Connection conn = this.connect();
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                data[0] = rs.getInt(1);
                data[1] = rs.getString(2);
                data[2] = rs.getString(3);
                data[3] = rs.getInt(4);
                data[4] = rs.getInt(5);
                data[5] = rs.getString(6);
                data[6] = rs.getInt(7);
                dtm.addRow(data);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "An error occured" + "\n" + e.getMessage());
        }
        System.out.println("Finished");
        return table;
    }

    public JTable selectUsers() {
        System.out.println("Executing resultset");
        String sql = "SELECT * FROM socio";
        ResultSet rs;
        JTable table = new JTable();
        DefaultTableModel dtm = new DefaultTableModel();
        dtm.setRowCount(0);
        dtm.addColumn("Numsocio");
        dtm.addColumn("Nombre");
        dtm.addColumn("Apellidos");
        dtm.addColumn("Telefono");
        dtm.addColumn("Edad");
        dtm.addColumn("Fecha");

        table.setModel(dtm);
        Object[] data = new Object[6];
        try {
            Connection conn = this.connect();
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                data[0] = rs.getInt(1);
                data[1] = rs.getString(2);
                data[2] = rs.getString(3);
                data[3] = rs.getInt(4);
                data[4] = rs.getInt(5);
                data[5] = rs.getDate(6);
                dtm.addRow(data);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null,"An error occured"+"\n"+e.getMessage());
        }
        System.out.println("Finished");
        return table;
    }

    public JTable selectBorrowings() {
        System.out.println("Executing resultset");
        String sql = "SELECT * FROM prestamo";
        ResultSet rs;
        JTable table = null;
        table = new JTable();
        DefaultTableModel dtm = new DefaultTableModel();
        dtm.addColumn("Libro ISBN: ");
        dtm.addColumn("Socio numsocio: ");
        dtm.addColumn("Fecha prestamo");
        dtm.addColumn("Fecha devolcion");
        table.setModel(dtm);
        Object[] data = new Object[4];
        try {
            Connection conn = this.connect();
            Statement stmt = conn.createStatement();
        rs = stmt.executeQuery(sql);
            while (rs.next()) {
                data[0] = rs.getInt(1);
                data[1] = rs.getInt(2);
                data[2] = rs.getDate(3);
                data[3] = rs.getDate(4);
                dtm.addRow(data);

            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null,"An error occured"+"\n"+e.getMessage());
        }

        System.out.println("Finished");
        return table;
    }
}




