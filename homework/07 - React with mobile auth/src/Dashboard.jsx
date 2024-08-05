import { Button, Col, Container, Form, Row } from "react-bootstrap";
import Menu from "./Menu";
import { useFirebaseAuth } from "./hooks/useFirebaseAuth";
import { useEffect, useState } from "react";
import { addPhoneNumber } from "./repositories/UserRepository";
import { firebaseAuth } from "./firebase";
import { getAuth } from "firebase/auth/web-extension";
import { linkWithCredential, PhoneAuthProvider, RecaptchaVerifier } from "firebase/auth";
import { register } from "./repositories/AuthRepository";

const Dashboard = () => {
    const { profile } = useFirebaseAuth();
    const auth = getAuth();

    useEffect(() => {
        const verifier = new RecaptchaVerifier(auth, "sign-in-button", {
            size: "invisible",
        });
        window.recaptchaVerifier = verifier;

        return () => verifier.clear(); // Cleanup on unmount
    }, [auth]);

    const [phoneNumber, setPhoneNumber] = useState('')
    const [contactName, setContactName] = useState('')
    const [registerPhoneNumber, setRegisterPhoneNumber] = useState('')


    const postPhoneNumber = async () => {
        const bool = await addPhoneNumber(profile?.email, contactName, phoneNumber)
        if (bool) {
            alert("Contacto agregado")
        } else {
            alert("Contacto no agregado")
        }
    }

    const handleRegisterPhoneNumber = async () => {
        const appVerifier = window.recaptchaVerifier
        const provider = new PhoneAuthProvider(auth)
        const verificationId = await provider.verifyPhoneNumber(registerPhoneNumber, appVerifier)
        const phoneCredential = PhoneAuthProvider.credential(verificationId, "123456")
        linkWithCredential(auth.currentUser, phoneCredential).then(
            () => { alert("Número registrado") }
        ).catch(() => { alert("Error, prueba otra vez") })
    }
    return (
        <>
            <Menu />
            <Container>
                <Row>
                    <Col>
                        <h1>Dashboard</h1>
                        <p>Welcome {profile?.name} {profile?.lastName}</p>
                        <Form onSubmit={postPhoneNumber}>
                            <Form.Group>
                                <Form.Label>Name</Form.Label>
                                <Form.Control required value={contactName} onChange={(e) => setContactName(e.target.value)} type="text" placeholder="Enter contact name" />
                            </Form.Group>
                            <Form.Group>
                                <Form.Label>Phone Number</Form.Label>
                                <Form.Control required value={phoneNumber} onChange={(e) => setPhoneNumber(e.target.value)} type="number" placeholder="Enter phone number" />
                            </Form.Group>
                            <Button className="mt-2" variant="primary" type="submit">Register</Button>
                        </Form>

                        <h2>Register Phone Number</h2>
                        <Form onSubmit={handleRegisterPhoneNumber}>
                            <Form.Group>
                                <Form.Label>Phone Number</Form.Label>
                                <Form.Control required value={registerPhoneNumber} onChange={(e) => setRegisterPhoneNumber(e.target.value)} type="text" placeholder="Enter phone number to register" />
                            </Form.Group>
                            <Button id="sign-in-button" className="mt-2" variant="primary" type="submit">Register</Button>
                        </Form>
                    </Col>
                </Row>
            </Container>
        </>
    );
}

export default Dashboard;