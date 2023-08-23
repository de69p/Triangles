package org.example;

import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.IntStream;

public class Triangles extends PApplet {

    // Main method to launch the application
    public static void main(String[] args) {
        PApplet.main("org.example.Triangles");
    }

    ArrayList<Triangle> triangleObjects = new ArrayList<>();

    class Triangle {
        PVector[] vertices;
        int fillCol;
        float rotationAngle = 0;
        boolean shouldRotate = false;
        float scaleFactor = 1;
        float targetScale = 1;
        float scaleSpeed = 0.05f;

        // Constructor to initialize the triangle with vertices
        Triangle(PVector[] verts) {
            vertices = verts;
            fillCol = randomColor();
        }

        // Generates a random color
        int randomColor() {
            return color(random(255), random(255), random(255));
        }

        // Calculates the center point of the triangle
        PVector center() {
            return new PVector((vertices[0].x + vertices[1].x + vertices[2].x) / 3,
                    (vertices[0].y + vertices[1].y + vertices[2].y) / 3);
        }

        // Checks if a given point is inside the triangle
        boolean containsPoint(PVector pt) {
            float detT = (vertices[1].y - vertices[2].y) * (vertices[0].x - vertices[2].x) + (vertices[2].x - vertices[1].x) * (vertices[0].y - vertices[2].y);
            float alpha = ((vertices[1].y - vertices[2].y) * (pt.x - vertices[2].x) + (vertices[2].x - vertices[1].x) * (pt.y - vertices[2].y)) / detT;
            float beta = ((vertices[2].y - vertices[0].y) * (pt.x - vertices[2].x) + (vertices[0].x - vertices[2].x) * (pt.y - vertices[2].y)) / detT;
            float gamma = 1.0f - alpha - beta;
            return alpha > 0 && beta > 0 && gamma > 0;
        }

        // Updates the triangle's properties each frame
        void update() {
            scaleFactor += (targetScale - scaleFactor) * scaleSpeed;
        }

        // Displays the triangle on the canvas
        void display() {
            pushMatrix();
            PVector centerPt = center();
            translate(centerPt.x, centerPt.y);
            scale(scaleFactor);
            if (shouldRotate) {
                handleRotation();
            }
            fill(fillCol);
            stroke(0); // Black outline
            strokeWeight(5); // Thicker outline
            translate(-centerPt.x, -centerPt.y);
            beginShape();
            Arrays.stream(vertices).forEach(v -> vertex(v.x, v.y)); // Using lambda expression for vertices
            endShape(CLOSE);
            popMatrix();
        }

        // Handles the rotation of the triangle
        void handleRotation() {
            rotate(rotationAngle);
            rotationAngle += 0.05F;
            if (rotationAngle >= TWO_PI) {
                shouldRotate = false;
                rotationAngle = 0;
            }
        }
    }

    // Sets up the application's window size
    public void settings() {
        size(1250, 1000);
    }

    // Initializes the triangles and any other setup logic
    public void setup() {
        initializeTriangles();
    }

    // Main drawing loop, updates and displays triangles
    public void draw() {
        background(175);
        triangleObjects.forEach(t -> {
            t.update();
            t.display();
        });
        handleHoverEffect();
    }

    // Handles mouse click event
    public void mousePressed() {
        handleClickEffect();
    }

    // Function to initialize triangle objects with predefined vertices.
    void initializeTriangles() {
        int[][][] triangleVertices = {
                {{978, 758}, {887, 916}, {1071, 917}},
                {{758, 758}, {667, 916}, {851, 917}},
                {{319, 758}, {228, 916}, {412, 917}},
                {{104, 758}, {13, 916}, {197, 917}},
                {{541, 758}, {449, 916}, {634, 917}},
                {{992, 715}, {1084, 878}, {1176, 716}},
                {{772, 715}, {864, 878}, {956, 716}},
                {{556, 715}, {648, 878}, {740, 716}},
                {{338, 715}, {430, 878}, {521, 716}},
                {{117, 715}, {209, 878}, {300, 716}},
                {{985, 537}, {894, 695}, {1078, 696}},
                {{765, 537}, {674, 695}, {858, 696}},
                {{331, 537}, {240, 695}, {424, 696}},
                {{111, 537}, {20, 695}, {204, 696}},
                {{548, 537}, {456, 695}, {641, 696}},
                {{1003, 495}, {1095, 656}, {1187, 496}},
                {{782, 495}, {874, 656}, {966, 496}},
                {{566, 495}, {658, 656}, {750, 496}},
                {{348, 495}, {440, 656}, {531, 496}},
                {{128, 495}, {220, 656}, {311, 496}},
                {{983, 316}, {892, 474}, {1076, 475}},
                {{763, 316}, {672, 474}, {856, 475}},
                {{329, 316}, {238, 474}, {422, 475}},
                {{109, 316}, {18, 474}, {202, 475}},
                {{546, 316}, {454, 474}, {639, 475}},
                {{1003, 274}, {1095, 435}, {1187, 275}},
                {{782, 274}, {874, 435}, {966, 275}},
                {{566, 274}, {658, 435}, {750, 275}},
                {{348, 274}, {440, 435}, {531, 275}},
                {{128, 274}, {220, 435}, {311, 275}},
                {{988, 96}, {897, 254}, {1081, 255}},
                {{768, 96}, {677, 254}, {861, 255}},
                {{334, 96}, {243, 254}, {427, 255}},
                {{114, 96}, {23, 254}, {207, 255}},
                {{551, 96}, {459, 254}, {644, 255}},
                {{1003, 54}, {1095, 215}, {1187, 55}},
                {{782, 54}, {874, 215}, {966, 55}},
                {{566, 54}, {658, 215}, {750, 55}},
                {{348, 54}, {440, 215}, {531, 55}},
                {{128, 54}, {220, 215}, {311, 54}}
        };

        Arrays.stream(triangleVertices).forEach(triangleVertex -> {
            PVector[] triangle = new PVector[3];
            IntStream.range(0, 3).forEach(j -> triangle[j] = new PVector(triangleVertex[j][0], triangleVertex[j][1]));
            triangleObjects.add(new Triangle(triangle));
        });
    }

    // Function to handle hover effect.
    void handleHoverEffect() {
        Triangle hoveredTriangle = triangleObjects.stream()
                .filter(t -> t.containsPoint(new PVector(mouseX, mouseY)))
                .peek(t -> t.targetScale = 1.5F)
                .findFirst()
                .orElse(null);

        triangleObjects.forEach(t -> {
            if (t != hoveredTriangle) t.targetScale = 1; // Reset target scale for other triangles
        });

        // If a triangle is hovered, move it to the front (end of the list)
        if (hoveredTriangle != null) {
            triangleObjects.remove(hoveredTriangle);
            triangleObjects.add(hoveredTriangle);
        }
    }

    // Function to handle click effect.
    void handleClickEffect() {
        for (Triangle t : triangleObjects) {
            if (t.containsPoint(new PVector(mouseX, mouseY))) {
                t.shouldRotate = true;
                t.fillCol = t.randomColor();
                triangleObjects.remove(t); // Move the clicked triangle to the front
                triangleObjects.add(t);
                break;
            }
        }
    }
}