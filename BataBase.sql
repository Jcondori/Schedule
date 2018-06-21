-- DATABASE NAME : DEMO
CREATE TABLE tareas (
    codigo        NUMBER PRIMARY KEY,
    fecha         DATE,
    descripcion   VARCHAR2(250 BYTE),
    estado        VARCHAR2(1 BYTE) DEFAULT 'A'
)