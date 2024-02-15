package org.example.springb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.client.FindIterable;
import com.mongodb.MongoException;
import com.mongodb.MongoTimeoutException;
import org.bson.Document;
import org.bson.conversions.Bson;
import static com.mongodb.client.model.Filters.*;


public class MongoDB{

    private static final String DATABASE_NAME = "videojuegos_bd";
    private static final String COLLECTION_NAME = "videojuegos";

    public static void main(String[] args) {
        try {
            MongoClient mongoClient = MongoClients.create("mongodb://admin:1234@localhost:27017/?authSource=miPrimerMondongo");
            //Se puede crear una conexión con usuario y contraseña
            // Establecer conexión con MongoDB
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            System.out.println("Conexión exitosa a la base de datos: " + database.getName());

            // Operaciones CRUD
            insertarJuego(database,"molon","Ps5",2023,"muert2", "destruccion");
            insertarJuego(database,"dragón amarillo","Ps5",4500,"muert2", "avion");
            insertarJuego(database,"call of duty","Ps5",5849,"corredor violento", "sal");
            insertarJuego(database,"zombies lentos","Ps5",2990,"sargento sereno", "destruccion");
            actualizarJuegoConVariosCampos(database,"owder wild");
            obtenerTodosLosJuegos(database);
            actualizarJuego(database, "Super Mario Odyssey");
            obtenerJuego(database, "owder wild");
            eliminarJuego(database, "molon");
            eliminarJuegoPorFiltro(database,"Ps5");
            obtenerTodosLosJuegos(database);

        } catch (MongoTimeoutException e) {
            System.err.println("Error: No se pudo conectar a MongoDB debido a un tiempo de espera.");
        } catch (MongoException e) {
            System.err.println("Error al interactuar con MongoDB: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error desconocido: " + e.getMessage());
        }
    }

    private static void insertarJuego(MongoDatabase database, String titulo, String plataforma, int lanzamiento, String logorUno, String logroDos) {
        try {
// Obtener la colección "videojuegos"
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

// Crear un nuevo documento para el juego
            Document nuevoJuego = new Document()
                    .append("titulo", titulo)
                    .append("plataforma", plataforma)
                    .append("lanzamiento", lanzamiento)
                    .append("logros", new Document()
                            .append(logorUno, logroDos));
// Insertar el nuevo juego en la colección
            collection.insertOne(nuevoJuego);
            System.out.println("Juego insertado correctamente.");

        } catch (MongoException e) {
            System.err.println("Error al insertar juego: " + e.getMessage());
        }
    }

    private static void obtenerTodosLosJuegos(MongoDatabase database) {
        try {
// Obtener la colección "videojuegos"
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

// Obtener todos los juegos de la colección
            FindIterable<Document> juegos = collection.find();

            System.out.println("\nTodos los juegos:");

// Imprimir la información de cada juego
            for (Document juego : juegos) {
                System.out.println(juego.toJson());
            }

        } catch (MongoException e) {
            System.err.println("Error al obtener juegos: " + e.getMessage());
        }
    }

    private static void actualizarJuego(MongoDatabase database, String titulo) {
        try {
// Obtener la colección "videojuegos"
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

// Crear un filtro para encontrar el juego específico
            Document filtro = new Document("titulo", titulo);

// Crear una actualización para cambiar el año de lanzamiento
            Document update = new Document("$set", new Document("lanzamiento", 2023));

// Realizar la actualización y obtener el resultado
            UpdateResult result = collection.updateOne(filtro, update);
            //y si quiero modificar varios campos
            System.out.println("\nNúmero de documentos modificados: " + result.getModifiedCount());

        } catch (MongoException e) {
            System.err.println("Error al actualizar juego: " + e.getMessage());
        }
    }
    private static void actualizarJuegoConVariosCampos(MongoDatabase database, String titulo) {
        try {
            // Obtener la colección "videojuegos"
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

            // Crear un filtro para encontrar el juego específico
            Document filtro = new Document("titulo", titulo);

            // Crear el documento de actualización con los cambios en varios campos
            Document update = new Document("$set", new Document()
                    .append("lanzamiento", 2058)
                    .append("plataforma", "ps9")
                    .append("logros.logro3", "nuevo logro")
                    .append("logros.logro1","Logro futuresco")
                    .append("nuevoCampo", "estoesnuevo"));

            // Realizar la actualización y obtener el resultado
            UpdateResult result = collection.updateOne(filtro, update);
            System.out.println("modificado correctament");

        } catch (MongoException e) {
            System.err.println("Error al actualizar juego: " + e.getMessage());
        }
    }


    private static void obtenerJuego(MongoDatabase database, String titulo) {
        try {
// Obtener la colección "videojuegos"
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

// Crear un filtro para encontrar el juego específico
            Document filtro = new Document("titulo", titulo);

// Encontrar el juego y obtener el primer resultado
            Document juego = collection.find(filtro).first();

            System.out.println("\nJuego encontrado:");
            System.out.println(juego.toJson());

        } catch (MongoException e) {
            System.err.println("Error al obtener juego: " + e.getMessage());
        }
    }

    private static void eliminarJuego(MongoDatabase database, String titulo) {
        try {
// Obtener la colección "videojuegos"
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

// Crear un filtro para encontrar el juego específico
            Document filtro = new Document("titulo", titulo);
//Y si quiero eliminar varios registros
// Eliminar el juego y obtener el resultado

            DeleteResult result = collection.deleteOne(filtro);

            System.out.println("\nNúmero de documentos eliminados: " + result.getDeletedCount());

        } catch (MongoException e) {
            System.err.println("Error al eliminar juego: " + e.getMessage());
        }
    }
    private static void eliminarJuegoPorFiltro(MongoDatabase database, String plataforma) {
        try {
// Obtener la colección "videojuegos"
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
            Bson query = eq("plataforma", plataforma);
            DeleteResult result = collection.deleteMany(query);
            System.out.println("\nNúmero de documentos eliminados: " + result.getDeletedCount());

        } catch (MongoException e) {
            System.err.println("Error al eliminar juego: " + e.getMessage());
        }
    }
}
