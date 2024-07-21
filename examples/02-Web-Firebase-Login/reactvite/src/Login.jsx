import { useState } from "react";
import { Button, Col, Container, Form, FormControl, FormLabel, Row } from "react-bootstrap";
import { firebaseAuth } from "./firebase";
import { signInWithEmailAndPassword } from "firebase/auth";

const Login = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const doSignIn = (e) => {
        e.preventDefault();
        const auth = firebaseAuth;
        signInWithEmailAndPassword(auth, email, password)
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
                    <Form onSubmit={doSignIn}>
                        <h1>Login</h1>
                        <FormLabel>Email</FormLabel>
                        <FormControl onChange={(e) => setEmail(e.target.value)} type="email" placeholder="Enter email" />
                        <FormLabel>Password</FormLabel>
                        <FormControl onChange={(e) => setPassword(e.target.value)} type="password" placeholder="Enter password" />
                        <Button variant="primary" type="submit">Sign in</Button>
                    </Form>
                </Col>
            </Row>
        </Container>
    );
}

export default Login;