import React, { useEffect, useState } from 'react';
import Tree from 'react-animated-tree';
import { CertificateService } from '../services/CertificateService';
import moment from 'moment';

function PKIHome() {
    const [data, setData] = useState({
        issued: []
    })
    const treeStyle = {
        padding: 16,
        fontFamily: 'arial',
    };

    useEffect(() => {
        CertificateService.get()
            .then(response => setData(response.data))
    }, [])

    return (
        <div style={treeStyle}>
            {generateTree(data)}
        </div>
    )
}

export default PKIHome;

function handleClick() {
    console.log('CLICK')
}

function generateTree(data) {
    let children = [];
    data.issued.forEach(child => {
        children.push(generateTree(child));
    });
    return <Tree key={data.id} content={generateContent(data)}>{children.length > 0 && children}</Tree>;
}

function generateContent(data) {
    const revoked = data.revoked;
    const expired = checkExpired(data);
    let tag = '';
    if (revoked) {
        tag = '[REVOKED]';
    } else if (expired) {
        tag = '[EXPIRED]';
    }
    const contentStyle = {
        cursor: 'pointer',
    };
    return <span onClick={handleClick} style={contentStyle}>{`${tag} ${data.commonName}`}</span>;
}

function checkExpired(data) {
    const now = moment();
    const start = moment(data.startDate);
    const end = moment(data.endDate);
    if (start.isAfter(now) || end.isBefore(now)) {
        return true;
    }
    return false;
}