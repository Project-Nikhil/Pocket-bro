import React, { useState } from 'react';
import { ChevronDown, ChevronUp, HelpCircle } from 'lucide-react';

interface FaqItem {
  question: string;
  answer: string;
}

export const FaqSection: React.FC = () => {
  const [openIndex, setOpenIndex] = useState<number | null>(0);

  const faqs: FaqItem[] = [
    {
      question: 'Is Pocket Brother available on Android?',
      answer:
        'Yes. Pocket Brother is designed natively for Android (supporting Android 7.0 Nougat / API level 24 up to Android 15 / API level 36). It is built with Jetpack Compose and distributed directly as a standalone Android application package (.apk).',
    },
    {
      question: 'How do I download the APK?',
      answer:
        'Simply tap the "Download for Android" button on this website from your Android device. Your phone\'s browser will download the file pocket-brother.apk directly into your phone\'s Downloads folder. There is no paywall, no account required, and no third-party app store needed.',
    },
    {
      question: 'How do I install the APK?',
      answer:
        'Once downloaded, tap the notification or open your phone\'s Files/Downloads app and tap pocket-brother.apk. If your browser or file manager asks for permission to install unknown apps, toggle "Allow from this source" and tap Install. The app icon will appear immediately on your home screen.',
    },
    {
      question: 'Does the app require an internet connection?',
      answer:
        'No. The primary chat companion and all daily life tools (Calendar, Reminders, Notes, Study Timer, and Memories) run completely offline on your device using the built-in Offline Brother Engine and on-device SQLite database. An internet connection is only needed if you choose to download additional quantized Gemma model weights or connect an optional online AI provider.',
    },
    {
      question: 'Where is my chat history stored?',
      answer:
        'Your chat history, private notes, reminders, and calendar events are stored 100% locally on your phone inside an isolated Room SQLite database (pocket_brother_db). Your personal data never leaves your device and is not uploaded to any remote server or cloud account.',
    },
    {
      question: 'Why does Android display a security warning?',
      answer:
        'Android displays a warning whenever you install an application outside the Google Play Store (known as sideloading). Because Pocket Brother is a personalized app created by your brother rather than published on Google Play, Google Play Protect flags it as an unrecognized developer. You can tap "More details" and "Install anyway" safely.',
    },
    {
      question: 'What should I do if installation fails?',
      answer:
        'First, ensure your phone has at least 150 MB of free internal storage. Second, check that your device runs Android 7.0 or higher. If you previously had an experimental or debug build installed with a different signature, uninstall that previous version first before tapping Install on the new APK.',
    },
    {
      question: 'How do I get a newer version?',
      answer:
        'When an update is released, visit this website and download the new pocket-brother.apk. Tap the downloaded file, and Android will offer to update the existing app without deleting your chat history or notes.',
    },
  ];

  return (
    <section id="faq" className="section">
      <div className="container">
        
        {/* Section Header */}
        <div className="section-header">
          <div className="pill-badge" style={{ marginBottom: '14px' }}>
            <HelpCircle size={14} color="#F5C58A" />
            <span>Got Questions?</span>
          </div>
          <h2>Frequently Asked Questions</h2>
          <p>
            Everything you need to know about downloading, installing, and using Pocket Brother on Android.
          </p>
        </div>

        {/* FAQ Accordion List */}
        <div style={{ maxWidth: '800px', margin: '0 auto', display: 'flex', flexDirection: 'column', gap: '14px' }}>
          {faqs.map((faq, index) => {
            const isOpen = openIndex === index;
            return (
              <div
                key={index}
                style={{
                  background: 'var(--surface-card)',
                  border: isOpen ? '1px solid var(--accent-amber)' : '1px solid var(--border-medium)',
                  borderRadius: 'var(--radius-lg)',
                  overflow: 'hidden',
                  transition: 'all 0.25s ease',
                  boxShadow: isOpen ? '0 8px 24px rgba(0,0,0,0.3)' : 'none',
                }}
              >
                <button
                  onClick={() => setOpenIndex(isOpen ? null : index)}
                  style={{
                    width: '100%',
                    padding: '20px 24px',
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'space-between',
                    textAlign: 'left',
                    color: 'var(--text-cream)',
                    background: 'none',
                    gap: '16px',
                  }}
                >
                  <span style={{
                    fontSize: '1.05rem',
                    fontWeight: 600,
                    color: isOpen ? 'var(--accent-amber)' : 'var(--text-cream)',
                  }}>
                    {faq.question}
                  </span>
                  <div style={{
                    width: '28px',
                    height: '28px',
                    borderRadius: '50%',
                    background: isOpen ? 'rgba(245, 197, 138, 0.15)' : 'var(--surface-elevated)',
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center',
                    flexShrink: 0,
                  }}>
                    {isOpen ? <ChevronUp size={16} color="var(--accent-amber)" /> : <ChevronDown size={16} color="var(--text-muted)" />}
                  </div>
                </button>

                {isOpen && (
                  <div style={{
                    padding: '0 24px 22px 24px',
                    color: 'var(--text-muted)',
                    fontSize: '0.925rem',
                    lineHeight: 1.65,
                    borderTop: '1px solid var(--border-subtle)',
                    paddingTop: '14px',
                  }}>
                    {faq.answer}
                  </div>
                )}
              </div>
            );
          })}
        </div>

      </div>
    </section>
  );
};
