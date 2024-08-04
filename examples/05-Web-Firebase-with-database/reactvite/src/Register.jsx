import { createUserWithEmailAndPassword } from "firebase/auth";
import { Button, Col, Container, Form, Row } from "react-bootstrap";
import { firebaseAuth } from "./firebase";
import { useState } from "react";

const Register = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const submitRegisterForm = (e) => {
        e.preventDefault();
        const auth = firebaseAuth;
        createUserWithEmailAndPassword(auth, email, password)
            .then((userCredential) => {
                const user = userCredential.user;
                console.log(user);
            }).catch((error) => {
                const errorCode = error.code;
                const errorMessage = error.message;
                console.log(errorCode, errorMessage);
            });
    }
    return (
        <Container>
            <Row>
                <Col>
                    <h1>Register</h1>
                    <Form onSubmit={submitRegisterForm}>
                        <Form.Group>
                            <Form.Label>Email</Form.Label>
                            <Form.Control value={email} onChange={(e) => setEmail(e.target.value)} type="email" placeholder="Enter email" />
                        </Form.Group>
                        <Form.Group>
                            <Form.Label>Password</Form.Label>
                            <Form.Control value={password} onChange={(e) => setPassword(e.target.value)} type="password" placeholder="Enter password" />
                        </Form.Group>
                        <Form.Group>
                            <Form.Label>Confirm Password</Form.Label>
                            <Form.Control value={confirmPassword} onChange={(e) => setConfirmPassword(e.target.value)} type="password" placeholder="Enter password" />
                        </Form.Group>
                        <Button variant="primary" type="submit">Register</Button>
                    </Form>
                </Col>
            </Row>
        </Container>
    );
}

export default Register;