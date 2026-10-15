import { useState } from 'react';
import {
  MessageCircle,
  Smile,
  Calendar,
  Bell,
  Timer,
  Clock,
  BookOpen,
  GraduationCap,
  Sparkles,
  Wrench,
  Cpu,
  Heart
} from 'lucide-react';

export const FeaturesSection = () => {
  const [activeMode, setActiveMode] = useState<'comfort' | 'funny' | 'irritative'>('comfort');
  const [expandedIndex, setExpandedIndex] = useState<number | null>(null);

  const modeConversations = {
    comfort: {
      title: 'Comfort Mode',
      accent: '#8EA7E9',
      border: '#585F96',
      bg: '#2A2B42',
      badge: 'When you are sad, stressed, or need a hug 🫂',
      messages: [
        { sender: 'sister', text: 'Hey bhaiya... feeling really overwhelmed with exams today 🥺', time: '10:14 PM' },
        { sender: 'brother', text: 'Hey chelli. Take a deep breath. You studied hard and you are capable of anything you set your mind to. ♡', time: '10:14 PM' },
        { sender: 'brother', text: 'I am always right here. Take a 15-minute break and drink some water, okay? 🫂', time: '10:15 PM' },
      ],
    },
    funny: {
      title: 'Funny Banter Mode',
      accent: '#E5B842',
      border: '#8C732E',
      bg: '#382F19',
      badge: 'Lighthearted sibling jokes & laughter 😂',
      messages: [
        { sender: 'sister', text: 'You know, you are actually the best brother in the world 💙', time: '04:22 PM' },
        { sender: 'brother', text: 'Of course I am, who else? Took you long enough to admit it 😂', time: '04:22 PM' },
        { sender: 'brother', text: 'Now please send 50 rupees for ice cream as consultation fee 🍨', time: '04:23 PM' },
      ],
    },
    irritative: {
      title: 'Playful Irritation Mode',
      accent: '#F87157',
      border: '#8E4035',
      bg: '#3E2320',
      badge: 'Classic brother teasing & drama 😈',
      messages: [
        { sender: 'sister', text: 'Bhaiya reply fast!! Stop ignoring my texts 😒', time: '02:08 PM' },
        { sender: 'brother', text: 'Oye bauni! Stop spamming my notifications or I will tell mom what you did yesterday 😈', time: '02:08 PM' },
        { sender: 'brother', text: 'And no, you are not getting my hoodie back. Ever. 😂', time: '02:09 PM' },
      ],
    },
  };

  const currentModeData = modeConversations[activeMode];

  const features = [
    {
      icon: MessageCircle,
      title: 'Brother Chat',
      description: 'Talk whenever you miss your brother.',
      detail: 'Sister-first chat screen with empathetic prompts, starter chips, voice clips, and instant offline replies.',
      tint: '#60A5FA',
      bg: '#1E2E42',
    },
    {
      icon: Smile,
      title: 'Three Chat Modes',
      description: 'Comfort, funny banter, or irritation.',
      detail: 'Tailor his personality anytime. Shifts to Comfort automatically if you mention feeling sad or overwhelmed.',
      tint: '#FBBF24',
      bg: '#3E3014',
    },
    {
      icon: Calendar,
      title: 'Calendar',
      description: 'Keep track of your events & dates.',
      detail: 'Monthly, weekly, and daily views. Keep track of college lectures, exam schedules, and talk-to-brother slots.',
      tint: '#FB923C',
      bg: '#3F2618',
    },
    {
      icon: Bell,
      title: 'Reminders',
      description: 'Remember assignments & tasks.',
      detail: 'Quick toggles for drinking water, exam revision, calling mom, and staying healthy throughout your day.',
      tint: '#FBBF24',
      bg: '#3C3415',
    },
    {
      icon: Timer,
      title: 'Study Timer',
      description: 'Focus with customizable sessions.',
      detail: '25-minute Pomodoro focus clock with short & long break intervals to keep your academic momentum going.',
      tint: '#F87171',
      bg: '#3D1E1E',
    },
    {
      icon: Clock,
      title: 'Clock & Stopwatch',
      description: 'Manage time with everyday tools.',
      detail: 'World clock and millisecond stopwatch for workout timing, study sprints, and routine tracking.',
      tint: '#A78BFA',
      bg: '#2C223E',
    },
    {
      icon: BookOpen,
      title: 'Notes & Diary',
      description: 'Thoughts & private diary in one place.',
      detail: 'Offline diary with mood tags, pinned thoughts, and personal reflections that stay safely on your device.',
      tint: '#FACC15',
      bg: '#3C3415',
    },
    {
      icon: GraduationCap,
      title: 'Study Planner',
      description: 'Organize subjects & exam dates.',
      detail: 'Track completed topics, target grades (A+), and countdowns to upcoming midterms and finals.',
      tint: '#34D399',
      bg: '#133632',
    },
    {
      icon: Sparkles,
      title: 'Memories',
      description: 'Keep meaningful notes & memories.',
      detail: 'A warm sanctuary of encouraging quotes, childhood memories, and real brother notes when you miss home.',
      tint: '#2DD4BF',
      bg: '#133632',
    },
    {
      icon: Wrench,
      title: 'Daily Tools',
      description: 'Practical everyday utilities.',
      detail: 'Built-in calculator, quick to-do checklists, local audio recordings, and weather glance.',
      tint: '#C084FC',
      bg: '#321F42',
    },
  ];

  return (
    <section id="features" className="section" style={{ backgroundColor: 'var(--bg-secondary)' }}>
      <div className="container">

        {/* Section Header */}
        <div className="section-header">
          <div className="pill-badge" style={{ marginBottom: '10px' }}>
            <Heart size={13} fill="#F5C58A" color="#F5C58A" />
            <span>Built In Jetpack Compose</span>
          </div>
          <h2>Everything You Need, In One App</h2>
          <p>
            More than just AI chat. A full daily companion designed specifically for everyday sister life.
          </p>
        </div>

        {/* Interactive Mode Tester Box */}
        <div id="demo" className="mode-demo-card">
          <div style={{ textAlign: 'center', marginBottom: '18px' }}>
            <span style={{
              fontSize: '0.75rem',
              color: 'var(--accent-amber)',
              fontWeight: 700,
              textTransform: 'uppercase',
              letterSpacing: '0.06em',
            }}>
              Interactive Chat Preview
            </span>
            <h3 style={{ fontSize: '1.35rem', color: 'var(--text-cream)', marginTop: '2px' }}>
              Test The 3 Chat Modes
            </h3>
            <p style={{ fontSize: '0.85rem', color: 'var(--text-muted)', marginTop: '4px' }}>
              Tap a mode to see real brother responses:
            </p>
          </div>

          {/* Mode Switcher Buttons */}
          <div className="mode-btn-row">
            <button
              onClick={() => setActiveMode('comfort')}
              className={`mode-pill-btn ${activeMode === 'comfort' ? 'active-comfort' : ''}`}
            >
              <span>🫂</span>
              <span>Comfort</span>
            </button>

            <button
              onClick={() => setActiveMode('funny')}
              className={`mode-pill-btn ${activeMode === 'funny' ? 'active-funny' : ''}`}
            >
              <span>😂</span>
              <span>Funny</span>
            </button>

            <button
              onClick={() => setActiveMode('irritative')}
              className={`mode-pill-btn ${activeMode === 'irritative' ? 'active-irritative' : ''}`}
            >
              <span>😈</span>
              <span>Teasing</span>
            </button>
          </div>

          {/* Interactive Chat Dialogue Bubble Box */}
          <div className="chat-dialogue-box" style={{ borderColor: currentModeData.border }}>
            {/* Header info */}
            <div style={{
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'space-between',
              paddingBottom: '10px',
              borderBottom: '1px solid var(--border-subtle)',
              marginBottom: '14px',
            }}>
              <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                <div style={{
                  width: '9px',
                  height: '9px',
                  borderRadius: '50%',
                  background: '#25D366',
                }} />
                <span style={{ fontSize: '0.85rem', fontWeight: 600, color: 'var(--text-cream)' }}>
                  Brother (Online)
                </span>
              </div>
              <span style={{
                fontSize: '0.75rem',
                color: currentModeData.accent,
                background: currentModeData.bg,
                padding: '2px 8px',
                borderRadius: 'var(--radius-full)',
                fontWeight: 600,
              }}>
                {currentModeData.title}
              </span>
            </div>

            {/* Messages */}
            <div style={{ display: 'flex', flexDirection: 'column', gap: '10px' }}>
              {currentModeData.messages.map((msg, idx) => (
                <div
                  key={idx}
                  style={{
                    alignSelf: msg.sender === 'sister' ? 'flex-end' : 'flex-start',
                    maxWidth: '86%',
                    background: msg.sender === 'sister' ? '#005C4B' : '#27211D',
                    color: 'var(--text-cream)',
                    borderRadius: '14px',
                    padding: '8px 12px',
                    fontSize: '0.875rem',
                    lineHeight: 1.4,
                    border: msg.sender === 'sister' ? 'none' : '1px solid var(--border-subtle)',
                    boxShadow: '0 2px 6px rgba(0,0,0,0.25)',
                  }}
                >
                  <p>{msg.text}</p>
                  <div style={{
                    fontSize: '0.675rem',
                    color: msg.sender === 'sister' ? 'rgba(255,255,255,0.7)' : 'var(--text-subtle)',
                    textAlign: 'right',
                    marginTop: '3px',
                  }}>
                    {msg.time} {msg.sender === 'sister' && '✓✓'}
                  </div>
                </div>
              ))}
            </div>

            {/* Footnote */}
            <div style={{
              marginTop: '12px',
              textAlign: 'center',
              fontSize: '0.75rem',
              color: 'var(--text-subtle)',
            }}>
              {currentModeData.badge}
            </div>
          </div>
        </div>

        {/* 10 Feature Cards Grid (Mobile 2-column or fluid cards) */}
        <div className="features-grid">
          {features.map((feat, index) => {
            const IconComp = feat.icon;
            const isExpanded = expandedIndex === index;
            return (
              <div
                key={index}
                className="feature-mobile-card"
                onClick={() => setExpandedIndex(isExpanded ? null : index)}
              >
                {/* Icon */}
                <div style={{
                  width: '40px',
                  height: '40px',
                  borderRadius: '12px',
                  background: feat.bg,
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'center',
                  marginBottom: '12px',
                  flexShrink: 0,
                }}>
                  <IconComp size={20} color={feat.tint} />
                </div>

                {/* Title */}
                <h3 style={{
                  fontSize: '1rem',
                  color: 'var(--text-cream)',
                  marginBottom: '4px',
                  fontWeight: 600,
                }}>
                  {feat.title}
                </h3>

                {/* Main Quote */}
                <p style={{
                  fontSize: '0.85rem',
                  color: 'var(--accent-amber)',
                  fontWeight: 500,
                  marginBottom: '8px',
                  lineHeight: 1.35,
                }}>
                  “{feat.description}”
                </p>

                {/* Detail */}
                <p style={{
                  fontSize: '0.78rem',
                  color: 'var(--text-muted)',
                  lineHeight: 1.45,
                }}>
                  {feat.detail}
                </p>
              </div>
            );
          })}
        </div>

        {/* AI Engine & RAM Architecture Note */}
        <div style={{
          marginTop: '36px',
          background: 'rgba(30, 24, 21, 0.7)',
          border: '1px solid var(--border-medium)',
          borderRadius: 'var(--radius-lg)',
          padding: '18px',
          display: 'flex',
          flexDirection: 'column',
          gap: '8px',
        }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
            <Cpu size={16} color="var(--accent-amber)" />
            <span style={{ fontSize: '0.9rem', fontWeight: 600, color: 'var(--text-cream)' }}>
              Dual AI Engine Architecture (Verified in App Code)
            </span>
          </div>
          <p style={{ fontSize: '0.825rem', color: 'var(--text-muted)', lineHeight: 1.55 }}>
            Pocket Brother includes a built-in <strong style={{ color: 'var(--text-cream)' }}>Instant Offline Core Engine</strong> that operates immediately without heavy downloads. It also supports on-device <strong style={{ color: 'var(--text-cream)' }}>Gemma Local LLMs</strong> with automatic RAM tier detection (Nano 2GB, Balanced 4GB, and Pro 8GB).
          </p>
        </div>

      </div>

      <style>{`
        .mode-demo-card {
          background: var(--surface-card);
          border: 1px solid var(--border-medium);
          border-radius: var(--radius-lg);
          padding: 20px 14px;
          margin-bottom: 40px;
          box-shadow: var(--shadow-card);
        }

        .mode-btn-row {
          display: grid;
          grid-template-columns: repeat(3, 1fr);
          gap: 8px;
          margin-bottom: 18px;
        }

        .mode-pill-btn {
          display: flex;
          align-items: center;
          justify-content: center;
          gap: 6px;
          padding: 10px 8px;
          border-radius: var(--radius-full);
          font-weight: 600;
          font-size: 0.85rem;
          border: 1px solid var(--border-subtle);
          background: var(--surface-elevated);
          color: var(--text-muted);
          transition: all 0.2s;
        }

        .active-comfort {
          border-color: #8EA7E9 !important;
          background: #2A2B42 !important;
          color: #8EA7E9 !important;
        }

        .active-funny {
          border-color: #E5B842 !important;
          background: #382F19 !important;
          color: #E5B842 !important;
        }

        .active-irritative {
          border-color: #F87157 !important;
          background: #3E2320 !important;
          color: #F87157 !important;
        }

        .chat-dialogue-box {
          max-width: 520px;
          margin: 0 auto;
          background: #100D0B;
          border: 1px solid var(--border-subtle);
          border-radius: var(--radius-md);
          padding: 14px 12px;
        }

        .features-grid {
          display: grid;
          grid-template-columns: 1fr;
          gap: 14px;
        }

        @media (min-width: 480px) {
          .features-grid {
            grid-template-columns: repeat(2, 1fr);
            gap: 14px;
          }
        }

        @media (min-width: 860px) {
          .features-grid {
            grid-template-columns: repeat(3, 1fr);
            gap: 20px;
          }
        }

        @media (min-width: 1100px) {
          .features-grid {
            grid-template-columns: repeat(4, 1fr);
          }
        }

        .feature-mobile-card {
          background: var(--surface-card);
          border: 1px solid var(--border-medium);
          border-radius: var(--radius-md);
          padding: 18px 16px;
          display: flex;
          flex-direction: column;
          transition: transform 0.2s, border-color 0.2s;
        }

        @media (hover: hover) {
          .feature-mobile-card:hover {
            border-color: var(--border-highlight);
            transform: translateY(-2px);
          }
        }

        @media (min-width: 768px) {
          .mode-demo-card {
            padding: 28px 24px;
            border-radius: var(--radius-xl);
          }
          .mode-btn-row {
            display: flex;
            justify-content: center;
          }
          .mode-pill-btn {
            padding: 10px 22px;
            font-size: 0.925rem;
          }
          .chat-dialogue-box {
            padding: 20px 18px;
            border-radius: var(--radius-lg);
          }
        }
      `}</style>
    </section>
  );
};
