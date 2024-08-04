import { useState } from "react";
import { Button, Col, Container, Form, FormControl, FormLabel, Row } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import { login } from "./repositories/AuthRepository";

const Login = () => {
    const navigate = useNavigate();
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');

    const doSignIn = (e) => {
        e.preventDefault();
        login(email, password).then(() => {
            navigate('/dashboard');
        }).catch((error) => {
            console.log(error);
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
                        <Button className="mt-2" variant="primary" type="submit">Sign in</Button>
                    </Form>
                </Col>
            </Row>
        </Container>
    );
}

export default Login;